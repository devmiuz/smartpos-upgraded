package uz.uzkassa.smartpos.feature.activitytype.selection.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigationScreen
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainRouter
import uz.uzkassa.smartpos.feature.activitytype.selection.domain.ActivityTypeSelectionInteractor
import uz.uzkassa.smartpos.feature.activitytype.selection.presentation.navigation.ActivityTypeSelectionRouter
import javax.inject.Inject

internal class ActivityTypeSelectionPresenter @Inject constructor(
    private val activityTypeSelectionInteractor: ActivityTypeSelectionInteractor,
    private val activityTypeSelectionRouter: ActivityTypeSelectionRouter,
    private val hasChildActivityTypesLazyFlow: Lazy<Flow<Boolean>>
) : MvpPresenter<ActivityTypeSelectionView>(), PlainRouter.NavigatorObserver {

    override fun onFirstViewAttach() =
        hasChildActivityTypes()

    override fun onObserveNavigation(screen: PlainNavigationScreen) {
        when (screen) {
            ActivityTypeSelectionRouter.Screens.ParentSelection -> Unit
            ActivityTypeSelectionRouter.Screens.ChildSelection -> Unit
        }
    }

    fun openParentSelection() =
        activityTypeSelectionRouter.openParentSelectionScreen()

    fun dismiss() =
        viewState.onDismissView()

    fun finishSelection() {
        activityTypeSelectionInteractor.getChildActivityTypes().let {
            activityTypeSelectionRouter.finishSelection(it)
            viewState.onDismissView()
        }
    }

    private fun hasChildActivityTypes() {
        hasChildActivityTypesLazyFlow.get()
            .onEach { viewState.onHasChildActivityTypes(it) }
            .launchIn(presenterScope)
    }

    override fun onDestroy() =
        activityTypeSelectionRouter.removeNavigatorObserver(this)
}