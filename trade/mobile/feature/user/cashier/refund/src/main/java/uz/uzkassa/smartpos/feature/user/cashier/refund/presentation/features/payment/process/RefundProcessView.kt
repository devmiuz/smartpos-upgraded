package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.process

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.process.RefundProcessDetails

internal interface RefundProcessView : MvpView {

    fun onRefundProcessDetailsDefined(refundProcessDetails: RefundProcessDetails)

    fun onLoadingProcess()

    fun onSuccessProcess()

    @OneExecution
    fun onErrorProcess(throwable: Throwable)

    fun onLoadingPrintLastProcess()

    fun onSuccessPrintLastProcess()

    @OneExecution
    fun onErrorPrintLastProcess(throwable: Throwable)

    @OneExecution
    fun onDismissView()
}