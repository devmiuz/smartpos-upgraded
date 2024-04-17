package uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination

import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product

data class ProductPagination(
    val isLast: Boolean,
    val isFirst: Boolean,
    val currentPage: Int,
    val size: Int,
    val products: List<Product>
)