package uz.uzkassa.apay.presentation.features.card

import moxy.MvpView
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.card_list.CardData
import uz.uzkassa.apay.data.model.card_list.CardListResponse

interface CardApayView : MvpView {

    fun onCardInfoDefined(cardInfo: CardInfo)

    fun onCardInfoLoading()

    fun onCardInfoError(throwable: Throwable)

    fun onCardInsertedEvent(isInserted: Boolean)

    fun onLoadingPay()

    fun onSuccessPay(apayUpdateBillResponse: ApayUpdateBillResponse)

    fun onErrorPay(throwable: Throwable)

    fun onSmsNotSending()

    fun onCardDataLoading()

    fun onCardDataSuccess(cardData: CardData)

    fun onCardDataError(throwable: Throwable)

    fun payButtonVisible()

    fun payButtonInVisible()

    fun openCheckPayDialog()

    fun onStartCreateBill()

    fun onSuccessCreateBill()

    fun onErrorCreateBill(it: Throwable)
}