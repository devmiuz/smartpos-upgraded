package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.widget.statelayout.StateLayout
import uz.uzkassa.smartpos.feature.activitytype.selection.R
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent.di.ParentSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.activitytype.selection.databinding.FragmentFeatureActivityTypeParentSelectionBinding as ViewBinding

internal class ParentSelectionFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_activity_type_parent_selection),
    IHasComponent<ParentSelectionComponent>, ParentSelectionView,
    StateLayout.OnErrorButtonClickListener {

    @Inject
    lateinit var lazySelectionPresenter: Lazy<ParentSelectionPresenter>
    private val presenter by moxyPresenter { lazySelectionPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.setParentActivityType(it) }
    }

    override fun getComponent(): ParentSelectionComponent =
        ParentSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { recyclerViewDelegate.onCreate(recyclerView, savedInstanceState) }
    }

    override fun onErrorButtonClick(stateLayout: StateLayout) =
        presenter.getParentActivityTypes()

    override fun onLoadingActivityTypes() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessActivityTypes(activityTypes: List<ActivityType>) =
        recyclerViewDelegate.onSuccess(activityTypes)

    override fun onErrorActivityTypes(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getParentActivityTypes() }

    companion object {

        fun newInstance() =
            ParentSelectionFragment().withArguments()
    }
}