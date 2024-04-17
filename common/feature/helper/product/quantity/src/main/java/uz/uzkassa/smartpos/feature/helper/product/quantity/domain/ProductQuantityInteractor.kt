package uz.uzkassa.smartpos.feature.helper.product.quantity.domain

import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.math.times
import uz.uzkassa.smartpos.core.utils.primitives.roundToBigDecimal
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.channel.ProductQuantityBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.channel.ProductUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.exception.ProductQuantityNotDefinedException
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantity
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantityResult
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureArgs
import java.math.BigDecimal
import javax.inject.Inject

internal class ProductQuantityInteractor @Inject constructor(
    private val productQuantityBroadcastChannel: ProductQuantityBroadcastChannel,
    private val productUnitBroadcastChannel: ProductUnitBroadcastChannel,
    productQuantityFeatureArgs: ProductQuantityFeatureArgs
) {
    private var productQuantity: ProductQuantity? = null
    private val uid: Long? = productQuantityFeatureArgs.uid
    private val categoryId: Long? = productQuantityFeatureArgs.categoryId
    private val categoryName: String? = productQuantityFeatureArgs.categoryName
    private val productId: Long? = productQuantityFeatureArgs.productId
    private val price: BigDecimal = productQuantityFeatureArgs.price
    private val productPrice: BigDecimal = productQuantityFeatureArgs.productPrice
    private val vatRate: BigDecimal? = productQuantityFeatureArgs.vatRate
    private val lastQuantity: Double = productQuantityFeatureArgs.quantity
    private val lastUnitId: Long? = productQuantityFeatureArgs.unitId
    private val barcode: String? = productQuantityFeatureArgs.barcode

    val productName: String = productQuantityFeatureArgs.productName

    init {
        with(productQuantityFeatureArgs) {
            productQuantityBroadcastChannel.setData(unitId, productPrice, lastQuantity, maxQuantity)
        }
    }

    fun setAmount(value: String) {
        val amount: BigDecimal = runCatching { value.toBigDecimal() }.getOrNull() ?: return
        productQuantityBroadcastChannel.offer(amount)
    }

    fun setQuantity(value: String) {
        if (value.endsWith(".")) {
            productQuantity = null
            return
        }

        val quantity: Double = runCatching { value.toDouble() }.getOrNull() ?: return
        productQuantityBroadcastChannel.offer(quantity)
    }

    fun setProductQuantity(productQuantity: ProductQuantity) {
        this.productQuantity = productQuantity
    }

    fun setProductUnits(productUnits: List<ProductUnit>) =
        productQuantityBroadcastChannel.setProductUnits(productUnits)

    fun setProductUnit(productUnit: ProductUnit) {
        productQuantityBroadcastChannel.setProductUnit(productUnit)
        productUnitBroadcastChannel.sendBlocking(productUnit)
    }

    fun getProductQuantityResult(): Flow<Result<ProductQuantityResult>> {
        val productQuantity: ProductQuantity? = productQuantity
        return when {
            productQuantity == null || productQuantity.quantity == 0.0 ->
                flowOf(Result.failure(ProductQuantityNotDefinedException()))
            else -> flowOf(productQuantity)
                .map {

                    val price: BigDecimal = it.productUnit?.price ?: price
                    val amount: BigDecimal =
                        if (it.productUnit != null) it.amount
                        else (price * it.quantity).roundToBigDecimal()

                    return@map ProductQuantityResult(
                        uid = uid,
                        categoryId = categoryId,
                        categoryName = categoryName,
                        productId = productId,
                        lastUnitId = lastUnitId,
                        amount = amount,
                        price = price,
                        productPrice = productPrice,
                        vatRate = vatRate,
                        barcode = barcode,
                        productName = productName,
                        productQuantity = it
                    )
                }
                .flatMapResult()
        }
    }
}