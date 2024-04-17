package uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.product_marking.data.exception.DuplicateProductMarkingException
import uz.uzkassa.smartpos.feature.product_marking.data.exception.NotFoundProductMarkingException
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureCallback
import uz.uzkassa.smartpos.feature.product_marking.domain.ProductMarkingInteractor
import javax.inject.Inject

internal class MarkingCameraScannerPresenter @Inject constructor(
    private val productMarkingInteractor: ProductMarkingInteractor,
    private val productMarkingFeatureCallback: ProductMarkingFeatureCallback
) : MvpPresenter<MarkingCameraScannerView>() {

    private var isCheckingScannedMark: Boolean = false

    override fun onFirstViewAttach() {
        viewState.setProductCount(
            totalQuantity = productMarkingInteractor.getTotalProductQuantity(),
            markedQuantity = productMarkingInteractor.getMarkedProductQuantity()
        )
    }

    fun onDismiss() {
        productMarkingFeatureCallback.onBack()
    }

    @FlowPreview
    fun setBarcodeResult(result: String) {
        if (isCheckingScannedMark) return

        productMarkingInteractor.setMarkingForProduct(result)
            .launchCatchingIn(presenterScope)
            .onStart {
//                viewState.onPauseCamera()
                isCheckingScannedMark = true
                viewState.onLoading()
            }
            .onSuccess {
                isCheckingScannedMark = false
                viewState.onMarkingSuccess()
                val markedQuantity = productMarkingInteractor.getMarkedProductQuantity()
                val totalQuantity = productMarkingInteractor.getTotalProductQuantity()
                viewState.setProductCount(
                    totalQuantity = totalQuantity,
                    markedQuantity = markedQuantity
                )
                if (markedQuantity == totalQuantity) {
                    productMarkingFeatureCallback.onFinish(productMarkingInteractor.getProductMarkingResult())
                }
//                else viewState.onResumeCamera()
            }
            .onFailure {
                isCheckingScannedMark = false
//                viewState.onResumeCamera()
                if (it is DuplicateProductMarkingException || it is NotFoundProductMarkingException)
                    viewState.onFailure(it)
                else viewState.onUndefinedError()
            }
    }
}