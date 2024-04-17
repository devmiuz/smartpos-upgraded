package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.product

import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product

internal sealed class ProductListResource {

    object Loading : ProductListResource()

    data class Success(val type: Type, val list: List<Product>) : ProductListResource() {

        enum class Type {
            FAVORITE, SEARCH
        }
    }

    data class Failure(val error: Throwable) : ProductListResource()
}