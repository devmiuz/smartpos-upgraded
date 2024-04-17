package uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner

import moxy.MvpView

internal interface MarkingCameraScannerView : MvpView {

    fun setProductCount(totalQuantity: Int, markedQuantity: Int)

    fun onResumeCamera()

    fun onPauseCamera()

    fun onLoading()

    fun onMarkingSuccess()

    fun onUndefinedError()

    fun onFailure(throwable: Throwable)
}