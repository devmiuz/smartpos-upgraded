package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction

internal interface PaymentProvidersView : MvpView {

    fun onLoadingProviders()

    fun onSuccessProviders(it: List<PaymentProvider>)

    fun onErrorProviders(throwable: Throwable)

    fun openTransactionDialog()
}