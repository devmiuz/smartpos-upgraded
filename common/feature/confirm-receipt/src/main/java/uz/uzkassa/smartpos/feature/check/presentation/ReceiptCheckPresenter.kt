package uz.uzkassa.smartpos.feature.check.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.*
import moxy.MvpPresenter
import moxy.presenterScope
import org.json.JSONObject
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureCallback
import uz.uzkassa.smartpos.feature.check.domain.ReceiptCheckInteractor
import javax.inject.Inject

internal class ReceiptCheckPresenter @Inject constructor(
    private val receiptCheckFeatureCallback: ReceiptCheckFeatureCallback,
    private val receiptCheckInteractor: ReceiptCheckInteractor,
    private val qrUrlLazyFlow: Lazy<Flow<String>>
) : MvpPresenter<ReceiptCheckView>() {

    override fun onFirstViewAttach() {
        qrUrlLazyFlow.get()
            .onEach {
                viewState.showQrScannerUrl(it)
                getReceiptData(it)
            }
            .launchIn(presenterScope)
    }

    fun getReceiptData(url: String) {
        receiptCheckInteractor.getReceiptByFiscalUrl(url)
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onLoadingReceiptDataByUrl()
            }.onSuccess {
                viewState.onSuccessReceiptDataByUrl(it)
            }.onFailure {
                viewState.onErrorReceiptDataByUrl(it)
            }
    }

    fun getCompanyData(companyId: Long) {
        receiptCheckInteractor.getCompany(companyId)
            .launchCatchingIn(presenterScope)
            .onSuccess {
                viewState.onSuccessCompanyData(it)
            }.onFailure {
                viewState.onErrorCompanyData(it)
            }
    }

    fun getCashierData(userId: Long) {
        receiptCheckInteractor.getUserByUserId(userId)
            .launchCatchingIn(presenterScope)
            .onSuccess {
                viewState.onSuccessCashierData(it)
            }.onFailure {
                viewState.onErrorUserData(it)
            }
    }

    fun createReceipt(receipt:Receipt) {
        receiptCheckInteractor.createReceipt(receipt)
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onLoadingCreateReceipt()
            }
            .onSuccess {
                val statusCode = JSONObject(it.string()).get("statusCode")
                if (statusCode == 200) {
                    viewState.onSuccessCreateReceipt()
                }
            }.onFailure {
                viewState.onErrorCreateResponse(it)
            }
    }


    fun backToRootScreen() =
        receiptCheckFeatureCallback.onBackFromCheckReceipt()

    fun openQrCameraScannerScreen() = receiptCheckFeatureCallback.openQrScannerScreen()


}