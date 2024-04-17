package uz.uzkassa.smartpos.feature.launcher.presentation.features.category

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.presentation.features.category.di.CategorySetupComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.launcher.databinding.FragmentFeatureLauncherProductCatalogSetupBinding as ViewBinding

internal class CategorySetupFragment
    : MvpAppCompatFragment(R.layout.fragment_feature_launcher_product_catalog_setup),
    IHasComponent<CategorySetupComponent>, CategorySetupView {

    @Inject
    lateinit var lazyPresenter: Lazy<CategorySetupPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    override fun getComponent(): CategorySetupComponent =
        CategorySetupComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            addProductsButton.setOnClickListener { presenter.openCategorySetupScreen() }
        }
    }

    override fun onLoadingCurrentBranch() =
        loadingDialogDelegate.show()

    override fun onSuccessCurrentBranch() =
        loadingDialogDelegate.dismiss()

    override fun onErrorCurrentBranch(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            CategorySetupFragment().withArguments()
    }
}