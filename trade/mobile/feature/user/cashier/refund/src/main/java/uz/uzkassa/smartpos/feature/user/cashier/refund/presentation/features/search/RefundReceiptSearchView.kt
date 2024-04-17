package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search

import moxy.MvpView
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotAllowedException
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotFoundException

internal interface RefundReceiptSearchView : MvpView {

    fun onLoadingSearch()

    fun onSuccessSearch()

    fun onErrorSearchCauseUidNotDefined()

    fun onErrorSearchCauseNotFound(throwable: FiscalReceiptNotFoundException)

    fun onErrorSearchCauseNotAllowed(throwable: FiscalReceiptNotAllowedException)

    fun onErrorSearch(throwable: Throwable)
}