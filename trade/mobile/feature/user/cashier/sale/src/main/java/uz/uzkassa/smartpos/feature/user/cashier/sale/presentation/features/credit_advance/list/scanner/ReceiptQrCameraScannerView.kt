package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.scanner

import moxy.MvpView
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType

internal interface ReceiptQrCameraScannerView : MvpView {

    fun onCameraTypeChanged(cameraType: CameraType)

    fun onResumeCamera()

    fun onPauseCamera()

    fun onLoadingSearch()

    fun onSuccessSearch()

    fun onErrorSearch(throwable: Throwable)
}