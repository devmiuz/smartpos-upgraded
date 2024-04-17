package uz.uzkassa.smartpos.feature.product.list.presentation

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPagination

internal interface ProductListView : MvpView {

    fun onCategoryNameDefined(categoryName: String)

    fun onLoadingProducts()

    fun onSuccessProducts(productPagination: ProductPagination)

    fun onErrorProducts(throwable: Throwable)

    fun onNewProductDefined(product: Product)

    @OneExecution
    fun onUpdateProductsStateChanged(isUpdateAllowed: Boolean)

    fun onLoadingUpdateProducts()

    fun onSuccessUpdateProducts()

    fun onErrorUpdateProducts(throwable: Throwable)

    fun onDismissDeleteProductAlert()

    fun onLoadingDeleteProduct()

    fun onErrorDeleteProduct(throwable: Throwable)

    fun onShowWarningDialog()
}