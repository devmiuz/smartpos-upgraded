package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.scanner

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.search.RefundFiscalReceiptSearchInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter
import javax.inject.Inject

internal class ReceiptQrCameraScannerPresenter @Inject constructor(
    private val refundFiscalReceiptSearchInteractor: RefundFiscalReceiptSearchInteractor,
    private val refundRouter: RefundRouter
) : MvpPresenter<ReceiptQrCameraScannerView>() {

    fun handleResult(result: String) {
        refundFiscalReceiptSearchInteractor
            .getReceiptByFiscalUrl(result)
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onPauseCamera()
                viewState.onLoadingSearch()
            }
            .onSuccess {
                viewState.onSuccessSearch()
                refundRouter.openProductListScreen()
            }
            .onFailure { viewState.onErrorSearch(it) }
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
        refundRouter.backToReceiptSearchScreen()
}