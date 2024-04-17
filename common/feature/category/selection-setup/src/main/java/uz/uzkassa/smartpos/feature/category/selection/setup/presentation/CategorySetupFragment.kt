package uz.uzkassa.smartpos.feature.category.selection.setup.presentation

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
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.category.selection.setup.R
import uz.uzkassa.smartpos.feature.category.selection.setup.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.category.selection.setup.presentation.di.CategorySetupComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.category.selection.setup.databinding.FragmentFeatureCategorySelectionSetupBinding as ViewBinding

class CategorySetupFragment : MvpAppCompatFragment(R.layout.fragment_feature_category_selection_setup),
    IHasComponent<CategorySetupComponent>, CategorySetupView, Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CategorySetupPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.selectCategory(it) }
    }

    override fun getComponent(): CategorySetupComponent =
        CategorySetupComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                inflateMenu(
                    menuResId = R.menu.menu_feature_category_selection_setup,
                    listener = this@CategorySetupFragment
                )
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            stateLayout.setOnErrorButtonClickListener { presenter.getCategories() }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.save_menu_item -> presenter.setEnabledCategories().let { true }
        else -> false
    }

    override fun onLoadingCategories() =
        binding.stateLayout.setToLoading()

    override fun onSuccessCategories(categories: List<Category>) {
        recyclerViewDelegate.set(categories)
        binding.stateLayout.setToSuccess()
    }

    override fun onErrorCategories(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onLoadingEnableCategories() =
        loadingDialogDelegate.show()

    override fun onErrorEnableCategories(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            CategorySetupFragment().withArguments()
    }
}