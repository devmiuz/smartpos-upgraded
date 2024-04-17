package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.SearchViewDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.hideSoftInput
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.product.ProductListResource.Success.Type
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.di.ProductSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainProductSelectionBinding as ViewBinding

internal class ProductSelectionFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_main_product_selection),
    IHasComponent<ProductSelectionComponent>, ProductSelectionView,
    SearchViewDelegate.SearchViewActionsListener {

    @Inject
    lateinit var lazyPresenter: Lazy<ProductSelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val searchViewDelegate by lazy { SearchViewDelegate(this, this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onProductClicked = { presenter.addProduct(it) },
            onProductFavoriteClicked = { presenter.proceedProductFavorites(it) },
            onPageChanged = { presenter.getProducts(it) }
        )
    }

    override fun getComponent(): ProductSelectionComponent =
        ProductSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            searchViewDelegate.onCreate(searchView, savedInstanceState)
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            cancelButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    override fun onSearchViewQueryTextChange(query: String) =
        presenter.findProducts(query)

    override fun onSearchViewQueryTextSubmit(query: String) =
        presenter.findProducts(query)

    override fun onSearchViewClose() {

        searchViewDelegate.clearFocus()
        hideSoftInput()
    }

    override fun onLoadingProducts() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessProducts(mustClear: Boolean, type: Type, products: List<Product>) {
        binding.appBarLayout.visibility = View.VISIBLE
        recyclerViewDelegate.onSuccess(mustClear, type, products)
    }

    override fun onErrorProducts(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getProducts(0) }

    override fun onLoadingProceedFavorite() =
        loadingDialogDelegate.show()

    override fun onSuccessProceedFavorite(product: Product) {
        loadingDialogDelegate.dismiss()
        recyclerViewDelegate.update(product)
    }

    override fun onErrorProceedFavorite(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            ProductSelectionFragment().withArguments()
    }
}