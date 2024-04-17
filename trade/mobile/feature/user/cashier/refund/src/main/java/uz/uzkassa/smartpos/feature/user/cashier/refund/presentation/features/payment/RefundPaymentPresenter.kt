package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.confirmation.SupervisorConfirmationState
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.RefundPayment
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount.RefundPaymentAmount
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.payment.RefundPaymentInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter
import java.math.BigDecimal
import javax.inject.Inject

internal class RefundPaymentPresenter @Inject constructor(
    private val amountLazyFlow: Lazy<Flow<Amount>>,
    private val refundPaymentInteractor: RefundPaymentInteractor,
    private val refundPaymentLazyFlow: Lazy<Flow<RefundPayment>>,
    private val refundRouter: RefundRouter,
    private val supervisorConfirmationStateLazyFlow: Lazy<Flow<SupervisorConfirmationState>>
) : MvpPresenter<RefundPaymentView>() {

    override fun onFirstViewAttach() {
        getProvidedAmount()
        getProvidedReceipt()
        getProvidedSupervisorConfirmationState()
    }

    fun setRefundCardPaymentType() =
        showPaymentAmount(AmountType.CARD)

    fun setRefundCashPaymentType() =
        showPaymentAmount(AmountType.CASH)

    private fun showPaymentAmount(type: AmountType) {
        val amount: RefundPaymentAmount = refundPaymentInteractor.getRefundPaymentAmount(type)
        if (amount.payment.allowedAmount <= BigDecimal.ZERO) {
            viewState.onErrorPaymentType(amount.payment.type, type)
        } else {
            refundRouter.openPaymentAmountScreen(
                amount = amount.payment.amount,
                allowedAmount = amount.payment.allowedAmount,
                leftAmount = amount.leftAmount,
                totalAmount = amount.totalAmount,
                type = type
            )
        }
    }

    private fun getProvidedAmount() {
        amountLazyFlow.get()
            .onEach { refundPaymentInteractor.addAmount(it.amount, it.type) }
            .launchIn(presenterScope)
    }

    private fun getProvidedReceipt() {
        refundPaymentLazyFlow.get()
            .onEach { viewState.onGettingRefundReceipt() }
            .onEach {
                viewState.onRefundPaymentDefined(it)
                if (it.isPaymentReceived) {
                    openSupervisorConfirmationScreen()
                    viewState.onRefundAmountReceived()
                }
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedSupervisorConfirmationState() {
        supervisorConfirmationStateLazyFlow.get()
            .filter { it == SupervisorConfirmationState.CONFIRMED }
            .onEach { viewState.onShowProcessView() }
            .launchIn(presenterScope)
    }

    fun openSupervisorConfirmationScreen() =
        refundRouter.openSupervisorConfirmationScreen()

    fun dismissRefundFinishAlert() =
        viewState.onDismissRefundFinishAlert()

    fun backToRootScreen() = withChangeDetailsAllowed {
        refundPaymentInteractor.resetPayments()
        refundRouter.backToProductListScreen()
    }

    private inline fun withChangeDetailsAllowed(action: () -> Unit) {
        if (refundPaymentInteractor.isSaleDetailsChangeAllowed) action.invoke()
        else viewState.onShowRefundFinishAlert()
    }
}