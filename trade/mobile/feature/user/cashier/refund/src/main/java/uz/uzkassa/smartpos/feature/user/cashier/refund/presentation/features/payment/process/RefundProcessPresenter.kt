package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.process

import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.process.RefundProcessInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter
import javax.inject.Inject

internal class RefundProcessPresenter @Inject constructor(
    private val refundProcessInteractor: RefundProcessInteractor,
    private val refundRouter: RefundRouter
) : MvpPresenter<RefundProcessView>() {
    private var isReceiptCreated: Boolean = false

    override fun onFirstViewAttach() {
        viewState.onRefundProcessDetailsDefined(refundProcessInteractor.getRefundReceiptDetails())
        createReceipt()
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun createReceipt() {
        refundProcessInteractor
            .createReceipt()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingProcess() }
            .onSuccess {
                deleteMarkings()
            }
            .onFailure { viewState.onErrorProcess(it) }
    }

    fun printLastReceipt() {
        refundProcessInteractor
            .printLastReceipt()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingPrintLastProcess() }
            .onSuccess { viewState.onSuccessProcess() }
            .onFailure { viewState.onErrorPrintLastProcess(it) }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun deleteMarkings() {
        refundProcessInteractor
            .deleteProductMarkings()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingProcess() }
            .onSuccess {
                isReceiptCreated = true
                viewState.onSuccessProcess()
            }
            .onFailure { viewState.onErrorProcess(it) }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun dismiss() {
        flowOf(isReceiptCreated)
            .flatMapConcat { if (it) refundProcessInteractor.clearSaleData() else flowOf(Unit) }
            .onEach { viewState.onDismissView() }
            .launchIn(presenterScope)
    }

    override fun onDestroy() {
        if (isReceiptCreated)
            refundRouter.exit()
    }
}