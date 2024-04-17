package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type

interface CardTypeSelectionView : MvpView {

    fun onLoadingCardTypes()

    fun onSuccessCardTypes(cardTypes: List<Type>)

    fun onFailureCardTypes(throwable: Throwable)

    fun onLoadingPayment()

    fun onSuccessPayment()

    fun onFailurePayment(throwable: Throwable)

    @OneExecution
    fun onDismissView()
}