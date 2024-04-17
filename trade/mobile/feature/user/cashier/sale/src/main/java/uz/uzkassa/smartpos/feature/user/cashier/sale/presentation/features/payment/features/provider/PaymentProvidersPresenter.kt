package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider

import kotlinx.coroutines.channels.sendBlocking
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.amount.SalePaymentAmount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.transaction.TransactionHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.PaymentProvidersInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.SalePaymentInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import java.math.BigDecimal
import javax.inject.Inject

internal class PaymentProvidersPresenter @Inject constructor(
    private val cashierSaleRouter: CashierSaleRouter,
    private val paymentProvidersInteractor: PaymentProvidersInteractor,
    private val salePaymentInteractor: SalePaymentInteractor,
    private val args: CashierSaleFeatureArgs
) : MvpPresenter<PaymentProvidersView>() {

    private var currentPaymentProvider: PaymentProvider? = null

    override fun onFirstViewAttach() {
        getProviderList()
    }

    private fun getProviderList() {
        paymentProvidersInteractor
            .getProviders()
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onLoadingProviders()
            }.onSuccess {
                viewState.onSuccessProviders(it)
            }.onFailure {
                viewState.onErrorProviders(it)
            }
    }

    fun setTransactionId(transactionId: String) {
        currentPaymentProvider?.let {
            val amount = salePaymentInteractor.getAmount(ReceiptPayment.Type.CARD)

            args.amountBroadcastChannel.sendBlocking(
                Amount(
                    amount = amount.leftAmount,
                    leftAmount = BigDecimal.ZERO,
                    totalAmount = amount.totalAmount,
                    type = ReceiptPayment.Type.CARD,
                    changeAmount = BigDecimal.ZERO,
                    creditAdvanceHolder = amount.creditAdvanceHolder,
                    transactionHolder = TransactionHolder(
                        paymentProviderId = it.UNI_CODE.toInt(),
                        transactionId = transactionId
                    )
                )
            )
            backToRootScreen()
        }
    }

    fun selectProvider(paymentProvider: PaymentProvider) {
        currentPaymentProvider = paymentProvider
        viewState.openTransactionDialog()
    }

    fun backToRootScreen() {
        cashierSaleRouter.backToPaymentScreen()
    }
}