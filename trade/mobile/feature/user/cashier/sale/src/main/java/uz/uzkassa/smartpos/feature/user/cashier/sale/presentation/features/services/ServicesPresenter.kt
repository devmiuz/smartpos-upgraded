package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.service.Service
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.service.ServicesInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import javax.inject.Inject

internal class ServicesPresenter @Inject constructor(
    private val cashierSaleFeatureCallback: CashierSaleFeatureCallback,
    private val cashierSaleRouter: CashierSaleRouter,
    private val servicesInteractor: ServicesInteractor
) : MvpPresenter<ServicesView>() {

    override fun onFirstViewAttach() =
        getAvailableServices()

    private fun getAvailableServices() {
        servicesInteractor
            .getAvailableServices()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingAvailableServices() }
            .onSuccess { viewState.onSuccessAvailableServices(it) }
            .onFailure { viewState.onErrorAvailableServices(it) }

    }

    fun selectAvailableService(service: Service) {
        when (service) {
            Service.CASH_OPERATIONS -> cashierSaleFeatureCallback.onOpenCashOperations()
            Service.RECEIPT_DRAFT -> cashierSaleRouter.openReceiptDraftListScreen()
            Service.REFUND -> cashierSaleFeatureCallback.onOpenRefundScreen()
            Service.GTPOS -> {
                runCatching { cashierSaleRouter.openGTPOSScreenOrThrow() }
                    .onFailure { viewState.onErrorOpenService(it) }
            }
            Service.ADVANCE_CREDIT -> {
                cashierSaleRouter.openCreditAdvanceReceiptListScreen()
            }
            else -> Unit
        }
    }
}