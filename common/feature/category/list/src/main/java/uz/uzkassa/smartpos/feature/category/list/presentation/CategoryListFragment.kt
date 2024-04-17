package uz.uzkassa.smartpos.feature.category.list.presentation

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
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.setMessage
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.feature.category.list.R
import uz.uzkassa.smartpos.feature.category.list.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.category.list.presentation.di.CategoryListComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.category.list.databinding.FragmentFeatureCategoryListBinding as ViewBinding

class CategoryListFragment : MvpAppCompatFragment(R.layout.fragment_feature_category_list),
    IHasComponent<CategoryListComponent>,
    CategoryListView, Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CategoryListPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val actionsAlertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val deleteAlertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onCategoryClicked = { /* ignored */ },
            onCategoryMenuClicked = { presenter.showMenuAlert(it) },
            onProductListClicked = { presenter.openResultScreen(it) }
        )
    }

    override fun getComponent(): CategoryListComponent =
        CategoryListComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            stateLayout.setOnErrorButtonClickListener { presenter.getCategories() }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.menu_product_manage_category_list_add_category_menuitem ->
            presenter.showAddCategoryView().let { true }
        else -> false
    }

    override fun onLoadingCategories() =
        binding.stateLayout.setToLoading()

    override fun onSuccessCategories(categories: List<Category>) {
        toolbarDelegate.apply {
            inflateMenu(R.menu.menu_category_manage_category_list, this@CategoryListFragment)
            setTintMenuItemById(
                menuResId = R.id.menu_product_manage_category_list_add_category_menuitem,
                colorResId = requireContext().colorAccent
            )
        }
        recyclerViewDelegate.set(categories)
        binding.stateLayout.setToSuccess()
    }

    override fun onErrorCategories(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onShowMenuActionsAlert(category: Category) {
        actionsAlertDialogDelegate.apply {
            newBuilder {
                setTitle("\"${category.name}\"")
                setItems(R.array.fragment_feature_category_list_alert_menu_items) { _, which ->
                    presenter.proceedWhichAction(category, which)
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ -> presenter.dismissMenuAlert() }
                setOnDismissListener { presenter.dismissMenuAlert() }
            }
        }.show()
    }

    override fun onDismissMenuActionAlert() =
        actionsAlertDialogDelegate.dismiss()

    override fun onUpsertCategories(categories: List<Category>) =
        recyclerViewDelegate.set(categories)

    override fun onShowDeleteCategoryAlert(category: Category) {
        deleteAlertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_category_list_alert_delete_category_title)
                setMessage(
                    R.string.fragment_feature_category_list_alert_delete_category_message,
                    category.name
                )
                setNeutralButton(R.string.core_presentation_common_ok) { _, _ ->
                    presenter.deleteCategory(category)
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ ->
                    presenter.dismissDeleteCategoryAlert()
                }
                setOnDismissListener { presenter.dismissDeleteCategoryAlert() }
            }
        }.show()
    }

    override fun onDismissDeleteCategoryAlert() =
        deleteAlertDialogDelegate.dismiss()

    override fun onLoadingUpdateCategories() =
        loadingDialogDelegate.show()

    override fun onSuccessUpdateCategories(categories: List<Category>) {
        loadingDialogDelegate.dismiss()
        recyclerViewDelegate.set(categories)
    }

    override fun onErrorUpdateCategories(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            CategoryListFragment().withArguments()
    }
}