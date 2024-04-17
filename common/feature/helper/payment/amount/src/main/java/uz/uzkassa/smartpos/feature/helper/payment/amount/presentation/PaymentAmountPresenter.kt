package uz.uzkassa.smartpos.feature.helper.payment.amount.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureCallback
import uz.uzkassa.smartpos.feature.helper.payment.amount.domain.AmountInteractor
import javax.inject.Inject

internal class PaymentAmountPresenter @Inject constructor(
    private val amountInteractor: AmountInteractor,
    private val paymentAmountFeatureCallback: PaymentAmountFeatureCallback
) : MvpPresenter<PaymentAmountView>() {

    override fun onFirstViewAttach() {
        viewState.onAmountChanged(amountInteractor.amount)
        viewState.onAmountTypeSuccess(amountInteractor.amount.type)
//        proceedCreditAdvanceResult()
    }

    fun addPaymentAmount(value: String) {
        val amount: Amount = amountInteractor.addAmount(value)
        viewState.onAmountChanged(amount)
        viewState.onAmountValueAdded(amount.amount)
    }

    fun setPaymentAmount(value: String) {
        val amount: Amount = amountInteractor.setAmount(value)
        viewState.onAmountChanged(amount)

        viewState.onAmountValueChanged(amount.amount)
    }

//    fun proceedCreditAdvanceResult() {
//        amountInteractor
//            .proceedCreditAdvanceReceipt()
//            .launchCatchingIn(presenterScope)
//            .onSuccess {
//                paymentAmountFeatureCallback.onFinish(it)
//                viewState.onDismissView()
//            }
//            .onFailure { viewState.onErrorCauseAmountNotDefined() }
//    }


    fun proceedAmountResult() {
        amountInteractor
            .getAmountResult()
            .launchCatchingIn(presenterScope)
            .onSuccess {
                paymentAmountFeatureCallback.onFinish(it)
                viewState.onDismissView()
            }
            .onFailure { viewState.onErrorCauseAmountNotDefined() }
    }

    fun dismiss() =
        viewState.onDismissView()
}