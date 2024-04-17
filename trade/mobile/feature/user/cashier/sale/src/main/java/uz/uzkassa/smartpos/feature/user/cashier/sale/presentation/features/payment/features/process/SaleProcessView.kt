package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.process

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.process.SaleProcessDetails

internal interface SaleProcessView : MvpView {

    fun onSaleProcessDetailsDefined(details: SaleProcessDetails)

    fun onCreditAdvanceHolderDefined(creditAdvanceHolder: CreditAdvanceHolder?)

    fun onLoadingProcess()

    fun onSuccessProcess()

    @OneExecution
    fun onErrorProcess(throwable: Throwable)

    fun onLoadingPrintLastProcess()

    fun onSuccessPrintLastProcess()

    @OneExecution
    fun onErrorPrintLastProcess(throwable: Throwable)

    @OneExecution
    fun onDismissDialog()

    fun onSaleReceiptPrinted()

    fun onErrorCreditAdvanceProcess(throwable: Throwable)

    fun onSuccessCreditAdvanceProcess()

    fun stopLoading()

    fun onLoadingCreditAdvance()
}