package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner

import moxy.MvpView
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType

internal interface CameraScannerView : MvpView {

    fun onCameraTypeChanged(cameraType: CameraType)

    fun onResumeCamera()

    fun onPauseCamera()

    fun onLoadingProduct()

    fun onSuccessProduct()

    fun onErrorProductCauseNotFound()

    fun onErrorProduct(throwable: Throwable)
}