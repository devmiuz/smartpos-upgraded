package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import java.math.BigDecimal


internal interface CreditAdvanceListView : MvpView {

    @OneExecution
    fun onLoadingCreditAdvanceReceipts()

    @OneExecution
    fun onSuccessCreditAdvanceReceipts(receipts: List<Receipt>)

    @OneExecution
    fun onErrorCreditAdvanceReceipts(throwable: Throwable)

    @OneExecution
    fun onLoadingReceiptVerification()

    @OneExecution
    fun onSuccessReceiptVerification()

    @OneExecution
    fun onFailureReceiptVerification()

    @OneExecution
    fun onShowPaymentAmountDialog(receipt: Receipt)

    @OneExecution
    fun onShowAdvanceReceiptTypeDialog(receipt: Receipt, totalCost: BigDecimal)

    @OneExecution
    fun onLoadingRestore()

    @OneExecution
    fun onErrorRestore(throwable: Throwable)

    @OneExecution
    fun onSuccessRestore()

    @OneExecution
    fun onShowSearchDialog()

    @OneExecution
    fun onLoadingSearch()

    @OneExecution
    fun onErrorSearch(throwable: Throwable)

}