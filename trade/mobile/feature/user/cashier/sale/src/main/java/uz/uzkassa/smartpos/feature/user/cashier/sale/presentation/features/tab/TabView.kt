package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface TabView : MvpView {

    fun onLoadingUser()

    fun onSuccessUser(user: User)

    fun onErrorUser(throwable: Throwable)

    fun onLoadingSync()

    fun onSuccessSync()

    fun onErrorSync(throwable: Throwable)

    @OneExecution
    fun onOpenUserDrawer()

    @OneExecution
    fun onCloseUserDrawer()

    @OneExecution
    fun onLockUserDrawer(enable: Boolean)

    fun onShowDrawerActionNotAllowedAlert()

    fun onDismissDrawerActionNotAllowedAlert()

    fun onShowPauseShiftAlert()

    fun onDismissPauseShiftAlert()

    fun onLoadingPauseShift()

    fun onErrorPauseShift(throwable: Throwable)

    fun onShowUserLogoutAlert()

    fun onDismissUserLogoutAlert()

    fun onLoadingUserLogout()

    fun onErrorUserLogout(throwable: Throwable)

    fun onShiftFinishingFailed()
}