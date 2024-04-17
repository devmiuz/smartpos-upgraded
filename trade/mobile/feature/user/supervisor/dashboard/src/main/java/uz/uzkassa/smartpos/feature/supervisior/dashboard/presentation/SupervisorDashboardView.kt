package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

interface SupervisorDashboardView : MvpView {

    fun onLoadingUser()

    fun onSuccessUser(user: User)

    fun onErrorUser(throwable: Throwable)

    fun onLoadingSync()

    fun onSuccessSync()

    fun onErrorSync(throwable: Throwable)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onOpenDirectionDrawer()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onCloseDirectionDrawer()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onLockDirectionDrawer(enable: Boolean)

    fun onLoadingUserLogout()

    fun onSuccessUserLogout()

    fun onErrorUserLogout(throwable: Throwable)
}