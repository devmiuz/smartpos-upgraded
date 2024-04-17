package uz.uzkassa.apay.presentation.features.apay

import moxy.MvpView
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.card_list.CardListResponse

interface HomeApayView : MvpView {
    fun onStartCreateBill()
    fun onSuccessCreateBill()
    fun onErrorCreateBill(it: Throwable)
    fun onLoadingPay()
    fun onSuccessPay()
    fun onNotFound()
    fun onErrorPay(it: Throwable)
    fun showCardList(it: List<CardListResponse>)
    fun openCheckPayDialog()
    fun onLoadingUpdateBill()
    fun onSuccessUpdateBill(it: ApayUpdateBillResponse)
    fun onFailureUpdateBill(it: Throwable)
}