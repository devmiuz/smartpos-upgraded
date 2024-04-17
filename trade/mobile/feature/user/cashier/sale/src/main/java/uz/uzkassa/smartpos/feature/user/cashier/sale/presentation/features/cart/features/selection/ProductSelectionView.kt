package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.product.ProductListResource.Success.Type

internal interface ProductSelectionView : MvpView {

    fun onLoadingProducts()

    fun onSuccessProducts(mustClear: Boolean, type: Type, products: List<Product>)

    fun onErrorProducts(throwable: Throwable)

    fun onLoadingProceedFavorite()

    fun onSuccessProceedFavorite(product: Product)

    fun onErrorProceedFavorite(throwable: Throwable)
}