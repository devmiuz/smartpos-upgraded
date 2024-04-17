package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.activitytype.selection.domain.ActivityTypeSelectionInteractor
import uz.uzkassa.smartpos.feature.activitytype.selection.domain.parent.ActivityTypeParentInteractor
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.navigation.ActivityTypeSelectionRouter
import javax.inject.Inject

internal class ParentSelectionPresenter @Inject constructor(
    private val activityTypeSelectionInteractor: ActivityTypeSelectionInteractor,
    private val activityTypeParentInteractor: ActivityTypeParentInteractor,
    private val activityTypeSelectionRouter: ActivityTypeSelectionRouter
) : MvpPresenter<ParentSelectionView>() {

    override fun onFirstViewAttach() =
        getParentActivityTypes()

    fun getParentActivityTypes() {
        activityTypeParentInteractor
            .getParentActivityTypes()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingActivityTypes() }
            .onSuccess { viewState.onSuccessActivityTypes(it) }
            .onFailure { viewState.onErrorActivityTypes(it) }
    }

    fun setParentActivityType(activityType: ActivityType) {
        activityTypeSelectionInteractor.setParentActivityType(activityType)
        activityTypeSelectionRouter.openChildSelectionScreen()
    }
}