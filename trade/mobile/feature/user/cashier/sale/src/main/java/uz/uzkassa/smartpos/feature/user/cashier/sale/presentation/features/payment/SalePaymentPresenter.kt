package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment

import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.gtpos.model.currency.GTPOSCurrency
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource
import uz.uzkassa.smartpos.core.data.source.resource.company.CompanyStore
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.feature.sync.common.data.exception.CompanyNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.Discount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.SalePayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.amount.SalePaymentAmount
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.PaymentActionsInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.SalePaymentInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import java.math.BigDecimal
import javax.inject.Inject

internal class SalePaymentPresenter @Inject constructor(
    private val amountLazyFlow: Lazy<Flow<Amount>>,
    private val cashierSaleRouter: CashierSaleRouter,
    private val discountLazyFlow: Lazy<Flow<Discount>>,
    private val paymentActionsInteractor: PaymentActionsInteractor,
    private val receiptHeldLazyFlow: Lazy<Flow<Unit>>,
    private val salePaymentInteractor: SalePaymentInteractor,
    private val salePaymentLazyFlow: Lazy<Flow<SalePayment>>,
    private val companyStore: CompanyStore,
    private val gtposPaymentSource: GTPOSPaymentSource,
    private val billLazyFlow: Lazy<Flow<String>>
) : MvpPresenter<SalePaymentView>() {


    override fun onFirstViewAttach() {
        getProvidedAmount()
        getProvidedDiscount()
        getProvidedSalePayment()
        getProvidedReceiptHeld()
        listenBillId()
        getPaymentActions()
    }

    fun getPaymentActions() {
        val creditAdvanceHolder = salePaymentInteractor.getCreditAdvanceHolderFromSaleInteractor()
        if (creditAdvanceHolder != null) {
            getCreditAdvancePaymentActions()
        } else {
            getSalePaymentActions()
        }
        updatePaymentAmount()
    }

    private fun updatePaymentAmount() {
        viewState.onSalePaymentDefined(salePaymentInteractor.getSalePayment())
    }

    fun setFirstPayment(creditAdvanceHolder: CreditAdvanceHolder) {
        if (creditAdvanceHolder.paymentAmount <= salePaymentInteractor.getSaleTotalCost()) {
            salePaymentInteractor.setCreditAdvanceProps(creditAdvanceHolder)
            viewState.onSuccessFirstPayment()
        } else {
            viewState.onErrorFirstPayment()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCurrentCompany() {
        companyStore.getCurrentCompanyForPayment()
            .onStart { viewState.loadingApayChecking() }
            .onEach {
                val companyEntity = it?.companyEntity
                if (companyEntity?.paymentTypes.isNullOrEmpty()) {
                    viewState.showApayDialog(false)
                } else {
                    if (companyEntity?.paymentTypes?.contains("APAY") == true) {
                        viewState.showApayDialog(true)
                        openApayPayment()
                    } else {
                        viewState.showApayDialog(false)
                    }
                }
            }
            .catch {
                if (it is NotFoundHttpException) throw CompanyNotFoundException()
                else viewState.errorApayCheck(it)
            }
            .launchIn(presenterScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCurrentCompanyForUzCard() {
        companyStore.getCurrentCompanyForPayment()
            .onStart { viewState.loadingApayChecking() }
            .onEach {
                val companyEntity = it?.companyEntity
                if (companyEntity?.paymentTypes.isNullOrEmpty()) {
                    viewState.showApayDialog(false)
                } else {
                    if (companyEntity?.paymentTypes?.contains("APAY") == true) {
                        viewState.showApayDialog(true)
                        openUzcard()
                    } else {
                        viewState.showApayDialog(false)
                    }
                }
            }
            .catch {
                if (it is NotFoundHttpException) throw CompanyNotFoundException()
                else viewState.errorApayCheck(it)
            }
            .launchIn(presenterScope)
    }


    fun openApayPayment() = withChangeDetailsAllowed {
        val amount: SalePaymentAmount = salePaymentInteractor.getAmount(Type.CARD)
        val uniqueId = salePaymentInteractor.getUniqueId()
        cashierSaleRouter.openApayPaymentScreen(
            creditAdvanceHolder = amount.creditAdvanceHolder,
            amount = amount.payment.amount,
            leftAmount = amount.leftAmount,
            totalAmount = amount.totalAmount,
            type = Type.APAY,
            description = "Please Pay ${salePaymentInteractor.leftAmount} from A-Pay App",
            uniqueId = uniqueId
        )
    }

    fun openUzcard() = withChangeDetailsAllowed {
        val amount: SalePaymentAmount = salePaymentInteractor.getAmount(Type.UZCARD)
        cashierSaleRouter.openUzCardScreen(
            creditAdvanceHolder = amount.creditAdvanceHolder,
            amount = amount.payment.amount,
            leftAmount = amount.leftAmount,
            totalAmount = amount.totalAmount,
            type = Type.UZCARD,
            description = "Please Pay ${salePaymentInteractor.leftAmount} from Uzcard"
        )
    }


    fun showSaleProcessView() =
        viewState.onShowSaleProcessView()

    fun dismissSaleFinishAlert() =
        viewState.onDismissSaleFinishAlert()

    fun backToRootScreen() = withChangeDetailsAllowed {
        val creditAdvanceHolder = salePaymentInteractor.getCreditAdvanceHolderFromSaleInteractor()
        if (creditAdvanceHolder != null) {
            if (creditAdvanceHolder.isRepayment) {
                salePaymentInteractor.clearCreditAdvanceHolder()
                cashierSaleRouter.backToTabScreen()
            } else {
                viewState.onShowClearCreditAdvanceDialog()
            }
        } else {
            cashierSaleRouter.backToTabScreen()
        }
    }

    fun clearCreditAdvanceHolderAndGoBack() {
        salePaymentInteractor.clearCreditAdvanceHolder()
        cashierSaleRouter.backToTabScreen()
    }

    fun openProvidersScreen() = cashierSaleRouter.openPaymentProvidersScreen()

    private fun checkGTPOS(
        amount: BigDecimal,
        type: Type,
        creditAdvanceHolder: CreditAdvanceHolder?
    ) {
        gtposPaymentSource.request(
            GTPOSOperation.Request.Payment(
                amount,
                GTPOSCurrency.UZS,
                GTPOSOperation.Type.SALE
            )
        )
            .onEach {
                if (it.isSuccess) {
                    salePaymentInteractor.setAmount(amount, type, creditAdvanceHolder)
                } else {
                    viewState.onFailureGTPOS(it.errorOrUnknown())
                }
            }
            .catch {
                viewState.onFailureGTPOS(it)
            }
            .launchIn(presenterScope)
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun selectPaymentAction(paymentAction: PaymentAction) = withChangeDetailsAllowed {
        when (paymentAction) {
            PaymentAction.DISCOUNT -> {
                val discount = salePaymentInteractor.getDiscount()
                cashierSaleRouter.openPaymentDiscountScreen(
                    amount = with(salePaymentInteractor) { totalAmount - totalPaidAmount },
                    discountAmount = discount.getOrCalculateDiscountAmount,
                    discountPercent = discount.discountPercent,
                    discountType = discount.discountType
                )
            }
            PaymentAction.FISCAL_RECEIPT -> openProvider()
            PaymentAction.RECEIPT_DRAFT -> viewState.onShowReceiptDraftCreation()
            PaymentAction.ADVANCE -> viewState.onShowFirstPaymentDialog(ReceiptStatus.ADVANCE)
            PaymentAction.CREDIT -> viewState.onShowFirstPaymentDialog(ReceiptStatus.CREDIT)
        }
    }

    fun openCashPayment() = showPaymentAmount(Type.CASH)

    fun openCardPayment() = showPaymentAmount(Type.CARD)

    private fun openProvider() = viewState.openProviderDialog()

    fun openHumoPayment() = showPaymentAmount(Type.HUMO)

    private fun showPaymentAmount(type: Type) = withChangeDetailsAllowed {
        val amount: SalePaymentAmount = salePaymentInteractor.getAmount(type)
        cashierSaleRouter.openPaymentAmountScreen(
            amount = amount.payment.amount,
            leftAmount = amount.leftAmount,
            totalAmount = amount.totalAmount,
            type = type,
            creditAdvanceHolder = amount.creditAdvanceHolder
        )
    }

    fun proceedAmount(amount: Amount) {
        when (amount.type) {
            Type.HUMO ->
                checkGTPOS(amount.amount, amount.type, amount.creditAdvanceHolder)
            Type.CARD ->
                salePaymentInteractor.setAmount(
                    amount.amount,
                    amount.type,
                    amount.creditAdvanceHolder,
                    amount.transactionHolder
                )
            else -> {}
        }
    }

    private fun listenBillId() {
        billLazyFlow.get()
            .onEach {
                it
                salePaymentInteractor.setPaymentBillId(it)
            }
            .launchIn(presenterScope)
    }

    private fun getSalePaymentActions() {
        paymentActionsInteractor
            .getSalePaymentActions()
            .onEach { viewState.onPaymentActionsDefined(it) }
            .launchIn(presenterScope)
    }

    private fun getRetryPaymentActions() {
        paymentActionsInteractor
            .getRetryPaymentActions()
            .onEach { viewState.onPaymentActionsDefined(it) }
            .launchIn(presenterScope)
    }

    private fun getCreditAdvancePaymentActions() {
        paymentActionsInteractor
            .getCreditAdvancePaymentActions()
            .onEach {
                viewState.onPaymentActionsDefined(it)
            }
            .launchIn(presenterScope)
    }


    private fun getProvidedDiscount() {
        discountLazyFlow.get()
            .mapNotNull { it.discountArbitrary }
            .onEach {
                salePaymentInteractor.setDiscount(
                    it.discountAmount,
                    it.discountPercent,
                    it.discountType
                )
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedAmount() {
        amountLazyFlow.get()
            .onEach {
                when (it.type) {
                    Type.CASH, Type.APAY ->
                        salePaymentInteractor.setAmount(
                            it.amount,
                            it.type,
                            it.creditAdvanceHolder,
                            it.transactionHolder
                        )
                    Type.CARD, Type.HUMO ->
                        viewState.onShowConfirmationDialog(it)
                    else -> {}
                }
            }
            .launchIn(presenterScope)
    }


    private fun getProvidedSalePayment() {
        salePaymentLazyFlow.get()
            .onEach {
                viewState.onSalePaymentDefined(it)
                if (it.isPaymentReceived) {
                    getRetryPaymentActions()
                    viewState.onSalePaymentReceived()
                    viewState.onShowSaleProcessView()
                }
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedReceiptHeld() {
        receiptHeldLazyFlow.get()
            .onEach { backToRootScreen() }
            .launchIn(presenterScope)
    }

    private inline fun withChangeDetailsAllowed(action: () -> Unit) {
        if (salePaymentInteractor.isSaleDetailsChangeAllowed) action.invoke()
        else viewState.onShowSaleFinishAlert()
    }
}