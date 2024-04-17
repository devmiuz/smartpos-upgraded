package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection

import android.widget.Toast
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.product.ProductListResource
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.selection.ProductSelectionInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import javax.inject.Inject

internal class ProductSelectionPresenter @Inject constructor(
    private val productSelectionInteractor: ProductSelectionInteractor,
    private val cashierSaleRouter: CashierSaleRouter,
    private val unitLazyFlow: Lazy<Flow<Unit>>
) : MvpPresenter<ProductSelectionView>() {
    private var clear: Boolean = false

    override fun onFirstViewAttach() {
        getProvidedUnit()
        getProducts()
    }

    fun getProducts(page: Int) {
        productSelectionInteractor.setPage(page)
    }

    fun findProducts(query: String) {
        clear = true
        productSelectionInteractor.findProduct(query)
    }

    fun addProduct(product: Product) {
        cashierSaleRouter.openProductQuantityScreen(product)
    }

    fun proceedProductFavorites(product: Product) {
        productSelectionInteractor
            .changeProductFavoriteState(product)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingProceedFavorite() }
            .onSuccess { viewState.onSuccessProceedFavorite(it) }
            .onFailure { viewState.onErrorProceedFavorite(it) }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun getProducts() {
        productSelectionInteractor
            .getProducts()
            .onEach {
                when (it) {
                    is ProductListResource.Loading ->
                        viewState.onLoadingProducts()
                    is ProductListResource.Success -> {
                        viewState.onSuccessProducts(clear, it.type, it.list)
                        clear = false
                    }
                    is ProductListResource.Failure ->
                        viewState.onErrorProducts(it.error)
                }
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedUnit() {
        unitLazyFlow.get()
            .onEach { backToRootScreen() }
            .launchIn(presenterScope)
    }

    fun backToRootScreen() =
        cashierSaleRouter.backToTabScreen()
}