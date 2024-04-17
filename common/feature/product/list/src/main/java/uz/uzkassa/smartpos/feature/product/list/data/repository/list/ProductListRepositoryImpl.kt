package uz.uzkassa.smartpos.feature.product.list.data.repository.list

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductPaginationRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPagination
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureArgs
import javax.inject.Inject

internal class ProductListRepositoryImpl @Inject constructor(
    productListFeatureArgs: ProductListFeatureArgs,
    private val productPaginationRelationDao: ProductPaginationRelationDao
) : ProductListRepository {
    private val categoryId: Long = productListFeatureArgs.categoryId

    @FlowPreview
    override fun getProducts(page: Int): Flow<ProductPagination> {
        return flow {
            val relation: ProductPagination =
                productPaginationRelationDao
                    .getRelationsByCategoryId(
                        categoryId = categoryId,
                        page = page,
                        size = 10,
                        isAllowedForSale = false
                    )
                    .map()

            emit(relation)
        }
    }
}