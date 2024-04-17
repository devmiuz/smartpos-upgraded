package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list

import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.BadRequestHttpException
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.credit_advance.CreditAdvanceBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.credit_advance.list.CreditAdvanceListInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import java.math.BigDecimal
import javax.inject.Inject


internal class CreditAdvanceListPresenter @Inject constructor(
    private val cashierSaleRouter: CashierSaleRouter,
    private val creditAdvanceListInteractor: CreditAdvanceListInteractor,
    private val fiscalUrlLazyFlow: Lazy<Flow<String>>
) : MvpPresenter<CreditAdvanceListView>() {

    private var isSearchApplied = false

    override fun onFirstViewAttach() {
        getCreditAdvanceReceipts()
        listenFiscalUrl()
    }

    private fun listenFiscalUrl() {
        fiscalUrlLazyFlow.get()
            .onEach { fiscalUrl ->
                if (fiscalUrl.trim().isEmpty()) {
                    getCreditAdvanceReceipts()
                } else {
                    searchReceipt(fiscalUrl = fiscalUrl)
                }
            }
            .launchIn(presenterScope)
    }

    fun backToRootScreen() {
        if (isSearchApplied) {
            isSearchApplied = false
            getCreditAdvanceReceipts()
        } else {
            val creditAdvanceHolder: CreditAdvanceHolder? =
                creditAdvanceListInteractor.getCreditAdvanceHolderFromSale()

            if (creditAdvanceHolder != null) {
                if (creditAdvanceHolder.isRepayment) {
                    creditAdvanceListInteractor.clearSaleInteractor()
                }
            }
            cashierSaleRouter.backToTabScreen()
        }
    }

    fun setReceiptForRestore() {
        val receipt = creditAdvanceListInteractor.selectedCreditAdvanceReceipt
        if (receipt != null) {
            if (receipt.baseStatus == ReceiptStatus.ADVANCE) {
                val totalPaid =
                    receipt.receiptDetails.map { it.amount }.sum().subtract(receipt.totalDiscount)
                viewState.onShowAdvanceReceiptTypeDialog(
                    receipt,
                    totalPaid
                )
            } else {
                viewState.onShowPaymentAmountDialog(receipt)
            }
        }
    }

    fun verifyReceiptForRestore(receipt: Receipt) {
        creditAdvanceListInteractor.setSelectedReceipt(receipt)
        receipt.originUid?.let {
            if (receipt.status == ReceiptStatus.PAID) {
                if (receipt.totalPaid == receipt.totalCost.subtract(receipt.totalDiscount)) {
                    if (receipt.baseStatus == ReceiptStatus.CREDIT) {
                        verifySaleIsCompleted(ReceiptStatus.CREDIT, it)
                    } else if (receipt.baseStatus == ReceiptStatus.ADVANCE) {
                        verifySaleIsCompleted(ReceiptStatus.ADVANCE, it)
                    }
                } else {
                    verifySaleIsCompleted(ReceiptStatus.CREDIT, it)
                }
            } else {
                verifySaleIsCompleted(receipt.status, it)
            }
        }
    }

    private fun verifySaleIsCompleted(receiptStatus: ReceiptStatus, uid: String) {
        if (receiptStatus == ReceiptStatus.ADVANCE) {
            getAdvanceSaleReceiptByUid(uid)
        }

        if (receiptStatus == ReceiptStatus.CREDIT) {
            getCreditSaleReceiptByUid(uid)
        }
    }

    fun getCreditAdvanceReceipts() {
        creditAdvanceListInteractor
            .getAdvanceCreditReceipts()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCreditAdvanceReceipts() }
            .onSuccess { viewState.onSuccessCreditAdvanceReceipts(it) }
            .onFailure { viewState.onErrorCreditAdvanceReceipts(it) }
    }

    private fun getCreditSaleReceiptByUid(uid: String) {
        creditAdvanceListInteractor
            .searchCreditAdvanceReceipt(
                receiptUid = uid
            )
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingReceiptVerification() }
            .onSuccess { receipts ->
                val saleReceipt = receipts.firstOrNull { it.originUid == it.uid }
                val creditReceipts = receipts.filter { it.originUid != it.uid }
                val actualPaidAmount = creditReceipts.map { it.totalPaid }.sum()
                if (saleReceipt == null) {
                    viewState.onErrorRestore(Throwable("sale receipt not found"))
                } else {
                    saleReceipt.let {
                        if (it.totalPaid <= actualPaidAmount) {
                            viewState.onFailureReceiptVerification()
                        } else {
                            viewState.onSuccessReceiptVerification()
                        }
                    }
                }
            }
            .onFailure {
                if (it is NotFoundHttpException) {
                    viewState.onSuccessReceiptVerification()
                } else {
                    viewState.onErrorRestore(it)
                }
            }
    }

    private fun getAdvanceSaleReceiptByUid(uid: String) {
        creditAdvanceListInteractor
            .getAdvanceReceiptByUid(uid)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingReceiptVerification() }
            .onSuccess {
                viewState.onFailureReceiptVerification()
            }
            .onFailure {
                if (it is NotFoundHttpException) {
                    viewState.onSuccessReceiptVerification()
                } else {
                    viewState.onErrorRestore(it)
                }
            }
    }

    fun searchReceipt(
        customerName: String = "",
        customerPhone: String = "",
        fiscalUrl: String = "",
        receiptUid: String = ""
    ) {
        isSearchApplied = true
        creditAdvanceListInteractor
            .searchCreditAdvanceReceipt(
                receiptUid = receiptUid,
                fiscalUrl = fiscalUrl,
                customerName = customerName,
                customerPhone = customerPhone
            )
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onLoadingSearch()
            }
            .onSuccess { receipts ->

                val fullReceiptList = receipts.toList()
                val uniqueReceiptList = receipts.distinctBy { receipt -> receipt.originUid }

                uniqueReceiptList.forEach { receipt ->
                    val totalPaid = fullReceiptList.filter { it.originUid == receipt.originUid }
                        .map { it.totalPaid }.sum()
                    receipt.totalPaid = totalPaid
                }

                viewState.onSuccessCreditAdvanceReceipts(uniqueReceiptList)
            }
            .onFailure {
                when (it) {
                    is BadRequestHttpException -> viewState.onSuccessCreditAdvanceReceipts(emptyList())
                    is NotFoundHttpException -> viewState.onSuccessCreditAdvanceReceipts(emptyList())
                    else -> viewState.onErrorSearch(it)
                }
            }
    }


    fun setPaidAmount(
        paidAmount: BigDecimal,
        receiptBaseStatus: ReceiptStatus,
        paidInFull: Boolean,
        customerName: String,
        customerPhone: String
    ) {
        creditAdvanceListInteractor.setCreditAdvanceProps(
            CreditAdvanceHolder(
                paymentAmount = paidAmount,
                status = receiptBaseStatus,
                paidInFull = paidInFull,
                isRepayment = true,
                customerPhone = customerPhone,
                customerName = customerName
            )
        )
        restoreCreditAdvanceReceipt()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun restoreCreditAdvanceReceipt() {
        creditAdvanceListInteractor
            .restoreCreditAdvanceReceipt()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingRestore() }
            .onSuccess {
                viewState.onSuccessRestore()
                cashierSaleRouter.openPaymentScreen()
            }
            .onFailure { viewState.onErrorRestore(it) }
    }

    fun openSearchDialog() {
        viewState.onShowSearchDialog()
    }

    fun openReceiptQrCameraScannerScreen() {
        isSearchApplied = false
        cashierSaleRouter.openReceiptQrCameraScannerScreen()
    }

}