package uz.uzkassa.smartpos.feature.check.presentation.features

import moxy.MvpView
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType

interface CameraQrScannerView : MvpView {

    fun onCameraTypeChanged(cameraType: CameraType)

    fun onResumeCamera()

    fun onPauseCamera()

    fun onLoadingProduct()

    fun onSuccessProduct()

    fun onErrorProductCauseNotFound()

    fun onErrorProduct(throwable: Throwable)
}