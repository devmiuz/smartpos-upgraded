package uz.uzkassa.smartpos.feature.product.list.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPagination
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.view.setTint
import uz.uzkassa.smartpos.feature.product.list.R
import uz.uzkassa.smartpos.feature.product.list.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.product.list.presentation.di.ProductListComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.product.list.databinding.FragmentFeatureProductListBinding as ViewBinding

class ProductListFragment : MvpAppCompatFragment(R.layout.fragment_feature_product_list),
    IHasComponent<ProductListComponent>, ProductListView, Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<ProductListPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onClicked = { t1, t2 -> presenter.openProductUpdate(t1, t2) },
            onPriceChanged = { t1, t2 -> presenter.updateProduct(t1, t2) },
            onPageChanged = { presenter.getProducts(it) }
        )
    }

    override fun getComponent(): ProductListComponent =
        ProductListComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setTitle("")
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.menu_product_manage_list_add_product_menuitem ->
            presenter.openProductCreation().let { true }
        R.id.menu_product_manage_list_update_products_menuitem ->
            presenter.updateProducts().let { true }
        else -> false
    }

    override fun onCategoryNameDefined(categoryName: String) =
        toolbarDelegate.setTitle(categoryName)

    override fun onLoadingProducts() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessProducts(productPagination: ProductPagination) {
        toolbarDelegate.apply {
            clearMenu()
            inflateMenu(R.menu.menu_feature_product_list, this@ProductListFragment)
            setTintMenuItemById(
                menuResId = R.id.menu_product_manage_list_add_product_menuitem,
                colorResId = requireContext().colorAccent
            )
            setTintMenuItemById(
                menuResId = R.id.menu_product_manage_list_update_products_menuitem,
                colorResId = requireContext().colorAccent
            )

            findMenuItemById(R.id.menu_product_manage_list_update_products_menuitem)?.let { it ->
                it.setTint(requireContext().colorAccent)
                it.isVisible = false
            }
        }

        recyclerViewDelegate.onSuccess(productPagination)
    }

    override fun onErrorProducts(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getProducts(1) }

    override fun onNewProductDefined(product: Product) =
        recyclerViewDelegate.upsertAll(listOf(product))

    override fun onUpdateProductsStateChanged(isUpdateAllowed: Boolean) {

        toolbarDelegate.apply {
            findMenuItemById(R.id.menu_product_manage_list_update_products_menuitem)
                ?.isVisible = isUpdateAllowed
        }
    }

    override fun onLoadingUpdateProducts() =
        loadingDialogDelegate.show()

    override fun onSuccessUpdateProducts() =
        loadingDialogDelegate.dismiss()

    override fun onErrorUpdateProducts(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissDeleteProductAlert() {
        alertDialogDelegate.dismiss()
    }

    override fun onLoadingDeleteProduct() =
        loadingDialogDelegate.show()

    override fun onErrorDeleteProduct(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onShowWarningDialog() {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.core_presentation_common_warning)
                setMessage(R.string.warning_message)
                setNeutralButton(R.string.core_presentation_common_ok) { _, _ ->
                    alertDialogDelegate.dismiss()
                }
            }
        }.show()
    }

    companion object {

        fun newInstance() =
            ProductListFragment().withArguments()
    }
}