package uz.uzkassa.smartpos.feature.product.list.domain.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPagination
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.product.list.data.repository.list.ProductListRepository
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureArgs
import javax.inject.Inject

internal class ProductListInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    productListFeatureArgs: ProductListFeatureArgs,
    private val productListRepository: ProductListRepository
) {
    val categoryName: String = productListFeatureArgs.categoryName

    fun getProducts(page: Int): Flow<Result<ProductPagination>> {
        return productListRepository
            .getProducts(page)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}