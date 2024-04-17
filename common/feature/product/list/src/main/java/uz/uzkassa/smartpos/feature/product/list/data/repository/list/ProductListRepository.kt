package uz.uzkassa.smartpos.feature.product.list.data.repository.list

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPagination

internal interface ProductListRepository {

    fun getProducts(page: Int): Flow<ProductPagination>
}