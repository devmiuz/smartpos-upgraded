package uz.uzkassa.smartpos.feature.check.presentation.features

import moxy.MvpPresenter
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureCallback
import javax.inject.Inject

class CameraQrScannerPresenter @Inject constructor(
    private val receiptCheckFeatureCallback: ReceiptCheckFeatureCallback
) : MvpPresenter<CameraQrScannerView>() {


    fun handleResult(result: String) {
        receiptCheckFeatureCallback.backToReceiptCheckScreen(result)
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
        receiptCheckFeatureCallback.backToReceiptCheckScreen("")
    }

//    private fun getProvidedUnit() {
//        unitLazyFlow.get()
//            .onEach { viewState.onResumeCamera() }
//            .launchIn(presenterScope)
//    }

}