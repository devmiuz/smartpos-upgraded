package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotAllowedException
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.ReceiptUidNotDefinedException
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.search.RefundReceiptSearchInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter
import javax.inject.Inject

internal class RefundReceiptSearchPresenter @Inject constructor(
    private val refundReceiptSearchInteractor: RefundReceiptSearchInteractor,
    private val refundRouter: RefundRouter
) : MvpPresenter<RefundReceiptSearchView>() {

    fun setUid(value: String) =
        refundReceiptSearchInteractor.setUid(value)

    fun getReceiptByUid() {
        refundReceiptSearchInteractor
            .getReceiptByUid()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingSearch() }
            .onSuccess {
                viewState.onSuccessSearch()
                refundRouter.openProductListScreen()
            }
            .onFailure {
                when (it) {
                    is ReceiptUidNotDefinedException ->
                        viewState.onErrorSearchCauseUidNotDefined()
                    is FiscalReceiptNotFoundException ->
                        viewState.onErrorSearchCauseNotFound(it)
                    is FiscalReceiptNotAllowedException ->
                        viewState.onErrorSearchCauseNotAllowed(it)
                    else -> viewState.onErrorSearch(it)
                }
            }
    }

    fun openReceiptQrCameraScannerScreen() =
        refundRouter.openReceiptQrCameraScannerScreen()

    fun backToRootScreen() =
        refundRouter.exit()
}