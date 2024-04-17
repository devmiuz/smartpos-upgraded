package uz.uzkassa.smartpos.feature.check.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse

internal interface ReceiptCheckView : MvpView {

    fun showQrScannerUrl(url: String)

    fun onLoadingReceiptDataByUrl()

    fun onSuccessReceiptDataByUrl(receipt: Receipt)

    fun onErrorReceiptDataByUrl(throwable: Throwable)

    fun onSuccessCompanyData(companyResponse: CompanyResponse)

    fun onErrorCompanyData(throwable: Throwable)

    fun onSuccessCashierData(userResponse: UserResponse)

    fun onErrorUserData(throwable: Throwable)

    fun onLoadingCreateReceipt()

    fun onSuccessCreateReceipt()

    fun onErrorCreateResponse(throwable: Throwable)


}