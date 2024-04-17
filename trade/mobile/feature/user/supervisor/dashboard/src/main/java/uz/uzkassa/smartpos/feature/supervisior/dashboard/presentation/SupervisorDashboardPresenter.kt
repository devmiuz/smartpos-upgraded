package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateListener
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sync.SyncInteractor
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.user.UserInteractor
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.navigation.SupervisorDashboardRouter
import javax.inject.Inject

class SupervisorDashboardPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val drawerStateDelegate: DrawerStateDelegate,
    private val supervisorDashboardRouter: SupervisorDashboardRouter,
    private val syncInteractor: SyncInteractor
) : MvpPresenter<SupervisorDashboardView>(),
    DrawerStateListener {

    override fun onFirstViewAttach() {
        supervisorDashboardRouter.openSalesDynamicsScreen()
        drawerStateDelegate.setStateListener(this)
        getUser()
    }

    override fun openDrawer() =
        viewState.onOpenDirectionDrawer()

    override fun closeDrawer() =
        viewState.onCloseDirectionDrawer()

    override fun setLockState(enable: Boolean) =
        viewState.onLockDirectionDrawer(enable)

    fun sync() {
        syncInteractor
            .sync()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingSync() }
            .onSuccess { viewState.onSuccessSync() }
            .onFailure { viewState.onErrorSync(it) }
    }

    fun logoutUser() {
        userInteractor
            .logoutUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUserLogout() }
            .onSuccess {
                viewState.onSuccessUserLogout()
                supervisorDashboardRouter.backToRootScreen()
            }
            .onFailure { viewState.onErrorUserLogout(it) }
    }

    private fun getUser() {
        userInteractor
            .getUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUser() }
            .onSuccess { viewState.onSuccessUser(it) }
            .onFailure { viewState.onErrorUser(it) }

    }
    fun openReceiptCheckScreen() = supervisorDashboardRouter.openReceiptCheckScreen()

    fun openBranchListScreen() =
        supervisorDashboardRouter.openBranchListScreen()

    fun openCategoryType() =
        supervisorDashboardRouter.openCategoryType()

    fun openNotificationsScreen() { /* ignored */
    }

    fun openUserListScreen() =
        supervisorDashboardRouter.openUserListScreen()

    fun openUserSettingsScreen() =
        supervisorDashboardRouter.openUserSettingsScreen()
}