package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter
import javax.inject.Inject

internal class CashierRefundPresenter @Inject constructor(
    private val refundRouter: RefundRouter
) : MvpPresenter<CashierRefundView>() {

    override fun onFirstViewAttach() =
        refundRouter.openReceiptSearchScreen()
}