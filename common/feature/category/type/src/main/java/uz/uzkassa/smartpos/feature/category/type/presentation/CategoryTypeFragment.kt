package uz.uzkassa.smartpos.feature.category.type.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.category.type.R
import uz.uzkassa.smartpos.feature.category.type.data.model.CategoryType
import uz.uzkassa.smartpos.feature.category.type.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.category.type.presentation.di.CategoryTypeComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.category.type.databinding.FragmentFeatureCategoryTypeBinding as ViewBinding

class CategoryTypeFragment : MvpAppCompatFragment(R.layout.fragment_feature_category_type),
    IHasComponent<CategoryTypeComponent>, CategoryTypeView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CategoryTypePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.setCategoryType(it) }
    }

    override fun getComponent(): CategoryTypeComponent =
        CategoryTypeComponent.create(XInjectionManager.findComponent())

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
        }
    }

    override fun onLoadingCategoryTypes() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessCategoryTypes(types: List<CategoryType>) =
        recyclerViewDelegate.onSuccess(types)

    override fun onErrorCategoryType(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable)

    companion object {

        fun newInstance() =
            CategoryTypeFragment().withArguments()
    }
}