package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.process

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.process.SaleProcessInteractor
import javax.inject.Inject

@OptIn(FlowPreview::class)
internal class SaleProcessPresenter @Inject constructor(
    private val saleProcessInteractor: SaleProcessInteractor
) : MvpPresenter<SaleProcessView>() {
    private var isReceiptCreated: Boolean = false
    private var creditAdvanceHolder = saleProcessInteractor.creditAdvanceHolder

    override fun onFirstViewAttach() {
        if (creditAdvanceHolder == null) {
            createReceipt()
        } else {
            if (creditAdvanceHolder!!.isRepayment) {
                if (creditAdvanceHolder!!.paidInFull) {
                    createReceipt()
                } else {
                    createCreditAdvanceReceipt()
                }
            } else {
                if (creditAdvanceHolder!!.status == ReceiptStatus.ADVANCE) {
                    createCreditAdvanceReceipt()
                } else {
                    createReceipt()
                }
            }
        }
        viewState.onSaleProcessDetailsDefined(saleProcessInteractor.getSaleProcessDetails())
    }

    fun printLastReceipt() {
        saleProcessInteractor
            .printLastReceipt()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingPrintLastProcess() }
            .onSuccess { viewState.onSuccessProcess() }
            .onFailure { viewState.onErrorPrintLastProcess(it) }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun dismissSaleProcessDialog() {
        flowOf(isReceiptCreated)
            .flatMapConcat {
                if (it)
                    saleProcessInteractor.clearSaleData()
                else
                    flowOf(Unit)
            }
            .onEach { viewState.onDismissDialog() }
            .launchIn(presenterScope)
    }

    fun dismissErrorDialog() {
        if (isReceiptCreated) {
            if (creditAdvanceHolder == null) {
                dismissSaleProcessDialog()
            } else {
                if (creditAdvanceHolder!!.isRepayment) {
                    dismissSaleProcessDialog()
                } else {
                    viewState.stopLoading()
                }
            }
        } else {
            dismissSaleProcessDialog()
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun createReceipt() {
        saleProcessInteractor
            .createReceipt()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingProcess() }
            .onSuccess {
                saveMarkings()
            }
            .onFailure {
                viewState.onErrorProcess(it)
            }
    }

    fun createCreditAdvanceReceipt() {
        saleProcessInteractor
            .createCreditAdvanceReceipt()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCreditAdvance() }
            .onSuccess {
                isReceiptCreated = true
                viewState.onSuccessCreditAdvanceProcess()
            }
            .onFailure {
                viewState.onErrorCreditAdvanceProcess(it)
            }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun saveMarkings() {
        saleProcessInteractor
            .saveProductMarkings()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingProcess() }
            .onSuccess {
                isReceiptCreated = true
                clearTempDataIfNecessary()
            }
            .onFailure { viewState.onErrorProcess(it) }
    }

    private fun clearTempDataIfNecessary() {
        saleProcessInteractor
            .clearTempDataIfNecessary()
            .launchCatchingIn(presenterScope)
            .onSuccess {
                viewState.onCreditAdvanceHolderDefined(creditAdvanceHolder)
                viewState.onSaleReceiptPrinted()
                //credit or advance
            }
            .onFailure {
                //sale
                viewState.onSuccessProcess()
            }
    }
}