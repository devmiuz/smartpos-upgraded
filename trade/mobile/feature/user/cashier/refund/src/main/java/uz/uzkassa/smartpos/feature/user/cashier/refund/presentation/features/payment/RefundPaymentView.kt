package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.RefundPayment

internal interface RefundPaymentView : MvpView {

    fun onGettingRefundReceipt()

    fun onRefundPaymentDefined(refundPayment: RefundPayment)

    fun onRefundAmountReceived()

    @OneExecution
    fun onShowProcessView()

    @OneExecution
    fun onShowRefundFinishAlert()

    fun onDismissRefundFinishAlert()

    fun onErrorPaymentType(required: AmountType, found: AmountType)

}