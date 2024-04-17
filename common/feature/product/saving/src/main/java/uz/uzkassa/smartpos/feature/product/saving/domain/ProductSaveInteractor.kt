package uz.uzkassa.smartpos.feature.product.saving.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.product.saving.data.exception.ProductSavingException
import uz.uzkassa.smartpos.feature.product.saving.data.mapper.mapToProductUnit
import uz.uzkassa.smartpos.feature.product.saving.data.model.ProductDetails
import uz.uzkassa.smartpos.feature.product.saving.data.repository.details.ProductDetailsRepository
import uz.uzkassa.smartpos.feature.product.saving.data.repository.save.ProductSavingRepository
import uz.uzkassa.smartpos.feature.product.saving.data.repository.save.params.SaveProductParams
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.properties.Delegates

internal class ProductSaveInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productDetailsRepository: ProductDetailsRepository,
    private val productSavingRepository: ProductSavingRepository
) {
    private val productUnits: MutableList<ProductUnit> = arrayListOf()
    private var product: Product by Delegates.notNull()
    private var productId: Long? = null
    private var barcode: String? = null
    private var vatBarcode: String? = null
    private var model: String? = null
    private var measurement: String? = null
    private var salesPrice: BigDecimal? = null
    private var vatRate: BigDecimal? = null
    private var name: String? = null
    private var category: Category? = null
    private var unit: Unit? = null
    private var weight: String? = null
    private var baseUnitIndex: Int? = null
    private var hasMark: Boolean = false
    private var committentTin: String? = null

    fun getProductDetails(): Flow<Result<ProductDetails>> {
        return productDetailsRepository
            .getProductDetails()
            .onEach { it ->
                category = it.category
                it.product?.also { product ->
                    this.product = product
                    this.productId = product.id
                    barcode = product.barcode
                    vatBarcode = product.vatBarcode
                    model = product.model
                    measurement = product.measurement
                    salesPrice = product.salesPrice
                    vatRate = product.vatRate
                    category = product.category
                    unit = product.unit
                    hasMark = product.hasMark
                    committentTin = product.commintentTin
                    setProductUnits(it.productUnits)
                    baseUnitIndex =
                        it.productUnits.indexOf(it.productUnits.lastOrNull { it.coefficient >= 1 })
                    if (baseUnitIndex == null) baseUnitIndex = 0
                }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getProductUnits(): List<ProductUnit> {
        return productUnits
    }

    fun getProductVATRate(): BigDecimal? {
        return vatRate
    }

    fun setHasMark(value: Boolean) {
        hasMark = value
    }

    fun setBarcode(value: String) {
        if (value.isNotBlank()) barcode = value
    }

    fun setVatBarcode(value: String) {
        vatBarcode = value
    }

    fun setCategory(value: Category) {
        category = value
    }

    fun setModel(value: String) {
        if (value.isNotBlank()) model = value
    }

    fun setMeasurement(value: String) {
        if (value.isNotBlank()) this.measurement = value
    }

    fun setSize(value: String) {
        if (value.isNotBlank()) weight = value
    }

    fun setSalesPrice(value: String) {
        val result: String = value.replace(" ", "")
        salesPrice = if (result.isNotBlank()) BigDecimal(result) else null
    }

    fun setName(value: String) {
        if (value.isNotBlank()) name = value
    }

    fun setCommittentTin(value: String) {
        committentTin = value
    }

    fun setProductUnits(list: List<ProductUnit>) {
        productUnits.apply { clear(); addAll(list.toMutableList()) }
    }

    fun setUnit(value: Unit) {
        productUnits.apply {
            if (value == unit) {
                val baseUnit = firstOrNull { it.isBase }
                if (baseUnit == null) {
                    val index: Int =
                        if (baseUnitIndex == null) 0
                        else checkNotNull(baseUnitIndex) + 1
                    add(index, value.mapToProductUnit())
                }
            } else {
                clear()
                add(value.mapToProductUnit())
            }
        }
        unit = value
    }

    fun setProductVATRate(value: BigDecimal?) {
        vatRate = value
    }

    fun saveProduct(): Flow<Result<Product>> {
        val exception = ProductSavingException(
            isBarcodeNotDefined = barcode == null,
            isCategoryNotDefined = category == null,
            isNameNotDefined = name == null,
            isPriceNotDefined = salesPrice == null,
            isUnitNotDefined = unit == null
        )

        return when {
            exception.isPassed -> flowOf(Result.failure(exception))
            else ->
                when (productId) {
                    null ->
                        productSavingRepository
                            .createProduct(
                                SaveProductParams(
                                    barcode = checkNotNull(barcode),
                                    model = model,
                                    measurement = measurement,
                                    salesPrice = salesPrice,
                                    vatRate = vatRate,
                                    name = checkNotNull(name),
                                    category = checkNotNull(category),
                                    unit = unit,
                                    hasMark = hasMark,
                                    productUnits = productUnits,
                                    vatBarcode = vatBarcode,
                                    committentTin = committentTin
                                )
                            )
                    else ->
                        productSavingRepository
                            .updateProduct(
                                SaveProductParams(
                                    product = product,
                                    barcode = barcode,
                                    model = model,
                                    hasMark = hasMark,
                                    measurement = measurement,
                                    salesPrice = salesPrice,
                                    vatRate = vatRate,
                                    name = name,
                                    category = category,
                                    unit = unit,
                                    productUnits = productUnits,
                                    vatBarcode = vatBarcode,
                                    committentTin = committentTin
                                )
                            )
                }.flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
        }
    }


}