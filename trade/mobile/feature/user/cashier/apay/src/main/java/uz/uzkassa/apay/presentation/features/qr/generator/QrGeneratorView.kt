package uz.uzkassa.apay.presentation.features.qr.generator

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse

interface QrGeneratorView : MvpView {

    fun onSuccessCreateBill(billId: String)

    fun onTick(progress: Int, time: String)

    fun openCheckPayDialog()
}