package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.scanner

import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.credit_advance.CreditAdvanceBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.credit_advance.list.CreditAdvanceListInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import javax.inject.Inject

internal class ReceiptQrCameraScannerPresenter @Inject constructor(
    private val saleRouter: CashierSaleRouter,
    private val creditAdvanceBroadcastChannel: CreditAdvanceBroadcastChannel
) : MvpPresenter<ReceiptQrCameraScannerView>() {

    fun handleResult(result: String) {
        presenterScope.launch {
            creditAdvanceBroadcastChannel.send(result)
        }
        backToRootScreen()
    }

    fun resumeCamera() =
        viewState.onResumeCamera()

    fun changeCameraType(cameraType: CameraType) {
        val type: CameraType = when (cameraType) {
            CameraType.BACK -> CameraType.FRONT
            CameraType.FRONT -> CameraType.BACK
        }
        viewState.onCameraTypeChanged(type)
    }

    fun backToRootScreen() =
        saleRouter.backToCreditAdvanceReceiptListScreen()
}