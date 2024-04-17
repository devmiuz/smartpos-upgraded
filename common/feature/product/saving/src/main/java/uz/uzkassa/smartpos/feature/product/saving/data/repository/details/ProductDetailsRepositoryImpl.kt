package uz.uzkassa.smartpos.feature.product.saving.data.repository.details

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.product.saving.data.model.ProductDetails
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureArgs
import javax.inject.Inject

internal class ProductDetailsRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRelationDao: CategoryRelationDao,
    private val categoryRestService: CategoryRestService,
    private val productRelationDao: ProductRelationDao,
    private val productEntityDao: ProductEntityDao,
    private val productRestService: ProductRestService,
    productSavingFeatureArgs: ProductSavingFeatureArgs,
    private val unitEntityDao: UnitEntityDao,
    private val unitRestService: UnitRestService
) : ProductDetailsRepository {
    private val branchId: Long = productSavingFeatureArgs.branchId
    private val categoryId: Long = productSavingFeatureArgs.categoryId
    private val productId: Long? = productSavingFeatureArgs.productId

    @FlowPreview
    override fun getProductDetails(): Flow<ProductDetails> {
        return flowOf(productId)
            .flatMapConcat { if (it != null) getProductByProductId(it) else flowOf(null) }
            .flatMapConcat { product -> getUnits().map { Pair(product, it) } }
            .flatMapConcat { pair ->
                val product: Product? = pair.first
                val units: List<Unit> = pair.second
                val isNotHaveDetails: Boolean = product?.isCustom == false
                return@flatMapConcat if (isNotHaveDetails)
                    getCategory()
                        .map { Pair(it, product) }
                        .map {
                            ProductDetails(
                                category = it.first,
                                product = it.second,
                                productUnits = it.second?.productUnits ?: emptyList(),
                                units = units
                            )
                        }
                else {
                    val category: Category? = product?.category ?: getCategory().single()

                    val productUnits: List<ProductUnit> =
                        mutableListOf<ProductUnit>()
                            .apply { product?.productUnits?.let { addAll(it) } }

                    val productDetails =
                        ProductDetails(
                            category = category,
                            product = product,
                            productUnits = productUnits,
                            units = units
                        )

                    return@flatMapConcat flowOf(productDetails)
                }
            }
    }

    @FlowPreview
    private fun getCategory(): Flow<Category?> {
        return flow { emit(categoryRelationDao.getRelationByCategoryId(categoryId)) }
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null) flowOf(it.map())
                else categoryRestService.getEnabledCategoriesByBranchId(branchId)
                    .onEach { categoryEntityDao.save(it) }
                    .mapNotNull { categoryRelationDao.getRelationByCategoryId(categoryId)?.map() }
            }
    }

    @FlowPreview
    private fun getProductByProductId(productId: Long): Flow<Product> {
        return flow { emit(productRelationDao.getRelationByProductId(productId)) }
            .switch {
                productRestService.getProductByProductId(productId, branchId)
                    .onEach { productEntityDao.save(it) }
                    .map { productRelationDao.getRelationByProductId(productId) }
            }
            .mapNotNull { it?.map() }
    }

    private fun getUnits(): Flow<List<Unit>> {
        return unitRestService
            .getUnits()
            .onEach { unitEntityDao.upsert(it.mapToEntities()) }
            .map { it.map() }
    }
}