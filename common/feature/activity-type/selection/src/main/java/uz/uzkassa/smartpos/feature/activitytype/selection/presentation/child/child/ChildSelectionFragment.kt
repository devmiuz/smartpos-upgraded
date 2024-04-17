package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.activitytype.selection.R
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child.di.ChildSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.activitytype.selection.databinding.FragmentFeatureActivityTypeChildSelectionBinding as ViewBinding

internal class ChildSelectionFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_activity_type_child_selection),
    IHasComponent<ChildSelectionComponent>, ChildSelectionView {

    @Inject
    lateinit var lazySelectionPresenter: Lazy<ChildSelectionPresenter>
    private val presenter by moxyPresenter { lazySelectionPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.setChildActivityType(it) }
    }

    override fun getComponent(): ChildSelectionComponent =
        ChildSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { recyclerViewDelegate.onCreate(recyclerView, savedInstanceState) }
    }

    override fun onClearChildActivityTypes() =
        recyclerViewDelegate.clear()

    override fun onLoadingChildActivityTypes() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessChildActivityTypes(childActivityTypes: List<ChildActivityType>) =
        recyclerViewDelegate.onSuccess(childActivityTypes)

    override fun onErrorChildActivityTypes(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable)

    override fun onChangeChildActivityType(childActivityType: ChildActivityType) =
        recyclerViewDelegate.update(childActivityType)

    companion object {

        fun newInstance() =
            ChildSelectionFragment().withArguments()
    }
}