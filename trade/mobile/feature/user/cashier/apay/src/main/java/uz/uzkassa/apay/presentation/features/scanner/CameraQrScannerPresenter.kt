package uz.uzkassa.apay.presentation.features.scanner

import dagger.Lazy
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.domain.QrScannerInteractor
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import javax.inject.Inject

internal class CameraQrScannerPresenter @Inject constructor(
    private val qrScannerInteractor: QrScannerInteractor,
    private val cashierApayRouter: CashierApayRouter,
    private val apayQRCodeFeatureArgs: CashierApayFeatureArgs
) : MvpPresenter<CameraQrScannerView>() {

    fun handleResult(result: String) {
        apayQRCodeFeatureArgs.clientIdBroadcastChannel.sendBlocking(ClientData(result))
        cashierApayRouter.clientId = result
        cashierApayRouter.backToHomeApayScreen()
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

    fun backToQrGeneratorScreen() {
        cashierApayRouter.backToHomeApayScreen()
    }
}