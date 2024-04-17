package uz.uzkassa.common.feature.category.selection.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.common.feature.category.selection.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.common.feature.category.selection.presentation.di.CategorySelectionComponent
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import javax.inject.Inject
import uz.uzkassa.common.feature.category.selection.databinding.FragmentFeatureCategorySelectionBinding as ViewBinding

class CategorySelectionFragment : MvpAppCompatDialogFragment(),
    IHasComponent<CategorySelectionComponent>, CategorySelectionView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<CategorySelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private lateinit var binding: ViewBinding
    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate: RecyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onCategoryClicked = {/*ignored*/ },
            onCategoryCheckClicked = { presenter.setCategory(it) })
    }

    override fun getComponent(): CategorySelectionComponent =
        CategorySelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { presenter.dismiss() }
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            stateLayout.setOnErrorButtonClickListener { presenter.getCategories() }
        }
    }

    override fun onLoadingCategories() =
        binding.stateLayout.setToLoading()

    override fun onSuccessCategories(categories: List<Category>) {
        recyclerViewDelegate.set(categories)
        binding.stateLayout.setToSuccess()
    }

    override fun onErrorCategories(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onDismissView() =
        dismiss()

    companion object {

        fun newInstance() =
            CategorySelectionFragment().withArguments()
    }
}