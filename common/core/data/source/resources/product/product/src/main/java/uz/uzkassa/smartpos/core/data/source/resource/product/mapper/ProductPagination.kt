package uz.uzkassa.smartpos.core.data.source.resource.product.mapper

import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPagination
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPaginationRelation

fun ProductPaginationRelation.map(): ProductPagination =
    ProductPagination(
        isLast = isLast,
        isFirst = isFirst,
        currentPage = currentPage,
        size = size,
        products = productRelations.map()
    )