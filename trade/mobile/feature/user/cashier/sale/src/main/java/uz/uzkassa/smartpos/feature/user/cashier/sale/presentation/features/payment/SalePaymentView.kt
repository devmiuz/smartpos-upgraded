package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.SalePayment
import java.math.BigDecimal

internal interface SalePaymentView : MvpView {

    fun onPaymentActionsDefined(actions: List<PaymentAction>)

    fun onSalePaymentDefined(salePayment: SalePayment)

    fun onShowConfirmationDialog(amount: Amount)

    @OneExecution
    fun onShowReceiptDraftCreation()

    fun onSalePaymentReceived()

    @OneExecution
    fun onShowSaleProcessView()

    @OneExecution
    fun onShowSaleFinishAlert()

    fun onDismissSaleFinishAlert()

    fun onFailureGTPOS(throwable: Throwable)

    fun loadingApayChecking()

    fun showApayDialog(isApay: Boolean)

    fun errorApayCheck(throwable: Throwable)

    @OneExecution
    fun onShowFirstPaymentDialog(receiptStatus: ReceiptStatus)

    @OneExecution
    fun openProviderDialog()

    fun onErrorFirstPayment()

    fun onSuccessFirstPayment()

    fun onShowClearCreditAdvanceDialog()
}