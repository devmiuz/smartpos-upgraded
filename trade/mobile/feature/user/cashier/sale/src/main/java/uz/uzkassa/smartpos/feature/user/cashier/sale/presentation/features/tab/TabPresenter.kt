package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateListener
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.tab.SaleStateInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sync.SyncInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.user.UserInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import javax.inject.Inject

internal class TabPresenter @Inject constructor(
    private val cashierSaleRouter: CashierSaleRouter,
    private val drawerStateDelegate: DrawerStateDelegate,
    private val saleStateInteractor: SaleStateInteractor,
    private val syncInteractor: SyncInteractor,
    private val userInteractor: UserInteractor
) : MvpPresenter<TabView>(), DrawerStateListener {


    override fun onFirstViewAttach() {
        getUser()
        drawerStateDelegate.setStateListener(this)
    }

    override fun openDrawer() =
        viewState.onOpenUserDrawer()

    override fun closeDrawer() =
        viewState.onCloseUserDrawer()

    override fun setLockState(enable: Boolean) =
        viewState.onLockUserDrawer(enable)

    fun sync() {
        syncInteractor
            .sync()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingSync() }
            .onSuccess { viewState.onSuccessSync() }
            .onFailure { viewState.onErrorSync(it) }
    }

    fun showPauseShiftAlert() =
        withDrawerActionAllowed { viewState.onShowPauseShiftAlert() }

    fun dismissPauseShiftAlert() =
        viewState.onDismissPauseShiftAlert()

    fun pauseShift() {
        userInteractor
            .pauseShift()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingPauseShift() }
            .onSuccess { cashierSaleRouter.backToRootScreen() }
            .onFailure { viewState.onErrorPauseShift(it) }
    }

    fun showUserLogoutAlert() =
        withDrawerActionAllowed { viewState.onShowUserLogoutAlert() }

    fun dismissUserLogoutAlert() =
        viewState.onDismissUserLogoutAlert()

    fun logout() {
        userInteractor
            .logout()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUserLogout() }
            .onSuccess { cashierSaleRouter.backToRootScreen() }
            .onFailure {
                saleStateInteractor.setCloseShiftError(it)
                viewState.onErrorUserLogout(it)
            }
    }

    fun openCameraScannerScreen() =
        cashierSaleRouter.openCameraScannerScreen()

    fun openSettingsScreen() =
        cashierSaleRouter.openSettingsScreen()

    fun openAutoPrintScreen() =
        cashierSaleRouter.openAutoPrint()

    fun checkState() {
        if (!saleStateInteractor.isExitFromSaleAllowed) {
            viewState.onOpenUserDrawer()
            viewState.onShiftFinishingFailed()
        }
    }

    fun dismissDrawerActionNotAllowedAlert() =
        viewState.onDismissDrawerActionNotAllowedAlert()

    private fun getUser() {
        userInteractor
            .getUser()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUser() }
            .onSuccess { viewState.onSuccessUser(it) }
            .onFailure { viewState.onErrorUser(it) }
    }

    private inline fun withDrawerActionAllowed(action: () -> Unit) {
        if (saleStateInteractor.isExitFromSaleAllowed) action.invoke()
        else viewState.onShowDrawerActionNotAllowedAlert()
    }
}