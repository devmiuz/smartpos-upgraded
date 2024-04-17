package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.domain.ActivityTypeSelectionInteractor
import uz.uzkassa.smartpos.feature.activitytype.selection.domain.child.ActivityTypeChildInteractor
import javax.inject.Inject

internal class ChildSelectionPresenter @Inject constructor(
    private val activityTypeSelectionInteractor: ActivityTypeSelectionInteractor,
    private val activityTypeChildInteractor: ActivityTypeChildInteractor,
    private val childActivityTypeLazyFlow: Lazy<Flow<ChildActivityType>>,
    private val parentActivityTypeIdLazyFlow: Lazy<Flow<Long>>
) : MvpPresenter<ChildSelectionView>() {

    override fun onFirstViewAttach() {
        getChildActivityType()
        getParentActivityTypeId()
    }

    fun setChildActivityType(childActivityType: ChildActivityType) =
        activityTypeSelectionInteractor.setChildActivityType(childActivityType)

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun getParentActivityTypeId() {
        parentActivityTypeIdLazyFlow.get()
            .onEach {
                viewState.onClearChildActivityTypes()
                viewState.onLoadingChildActivityTypes()
            }
            .flatMapMerge { activityTypeChildInteractor.getChildActivityTypesByParentId(it) }
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingChildActivityTypes() }
            .onSuccess { viewState.onSuccessChildActivityTypes(it) }
            .onFailure { viewState.onErrorChildActivityTypes(it) }
    }

    private fun getChildActivityType() {
        childActivityTypeLazyFlow.get()
            .onEach { viewState.onChangeChildActivityType(it) }
            .launchIn(presenterScope)
    }
}