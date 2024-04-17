package uz.uzkassa.apay.presentation.features.manual_card

import moxy.MvpView
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.card_list.CardData

interface ManualCardApayView : MvpView {

    fun onLoadingPay()

    fun onSuccessPay(apayUpdateBillResponse: ApayUpdateBillResponse)

    fun onErrorPay(throwable: Throwable)

    fun onSmsNotSending()

    fun onCardDataLoading()

    fun onCardDataSuccess(cardData: CardData)

    fun onCardDataError(throwable: Throwable)

    fun onStartCreateBill()

    fun onSuccessCreateBill()

    fun onErrorCreateBill(it: Throwable)
}