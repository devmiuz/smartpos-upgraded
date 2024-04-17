package uz.uzkassa.smartpos.feature.product.list.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureCallback
import uz.uzkassa.smartpos.feature.product.list.domain.list.ProductListInteractor
import uz.uzkassa.smartpos.feature.product.list.domain.save.ProductSavingInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class ProductListPresenter @Inject constructor(
    private val hasEnabledProductsLazyFlow: Lazy<Flow<Boolean>>,
    private val productLazyFlow: Lazy<Flow<Product>>,
    private val productListFeatureCallback: ProductListFeatureCallback,
    private val productListInteractor: ProductListInteractor,
    private val productSavingInteractor: ProductSavingInteractor
) : MvpPresenter<ProductListView>() {

    override fun onFirstViewAttach() {
        viewState.onCategoryNameDefined(productListInteractor.categoryName)
        hasUpdatedProducts()
        getProvidedProduct()
        getProducts(1)
    }

    fun getProducts(page: Int) {
        productListInteractor
            .getProducts(page)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingProducts() }
            .onSuccess {
                productSavingInteractor.setProducts(it.products)
                viewState.onSuccessProducts(it)
            }
            .onFailure { viewState.onErrorProducts(it) }
    }

    fun updateProduct(product: Product, price: BigDecimal) =
        productSavingInteractor.updateProduct(product, price)

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun updateProducts() {
        productSavingInteractor
            .updateProducts()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUpdateProducts() }
            .onSuccess { viewState.onSuccessUpdateProducts() }
            .onFailure { viewState.onErrorUpdateProducts(it) }
    }

    fun openProductCreation() {
//        productListFeatureCallback.onOpenProductCreation()
        viewState.onShowWarningDialog()
    }

    fun openProductUpdate(product: Product, price: BigDecimal) {
        viewState.onShowWarningDialog()
//        productListFeatureCallback.onOpenProductUpdate(product.id, price)
    }

    fun backToRootScreen() =
        productListFeatureCallback.onBackFromProductList()

    private fun hasUpdatedProducts() {
        hasEnabledProductsLazyFlow.get()
            .onEach { viewState.onUpdateProductsStateChanged(it) }
            .launchIn(presenterScope)
    }

    private fun getProvidedProduct() {
        productLazyFlow.get()
            .onEach { productSavingInteractor.addProduct(it) }
            .onEach { viewState.onNewProductDefined(it) }
            .launchIn(presenterScope)
    }
}