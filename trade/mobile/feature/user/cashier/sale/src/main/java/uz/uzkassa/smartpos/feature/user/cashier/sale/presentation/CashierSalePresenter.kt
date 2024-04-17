package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.user.UserInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import javax.inject.Inject

internal class CashierSalePresenter @Inject constructor(
    private val cashierSaleRouter: CashierSaleRouter,
    private val userInteractor: UserInteractor
) : MvpPresenter<CashierSaleView>() {

    override fun onFirstViewAttach() =
        getUser()

    private fun getUser() {
        flow {
            emit(userInteractor.getUser().first())
        }
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onLoadingUserLanguage()
            }
            .onSuccess {
                viewState.onSuccessUserLanguage(it.language)
                cashierSaleRouter.openTabScreen()
            }
    }
}