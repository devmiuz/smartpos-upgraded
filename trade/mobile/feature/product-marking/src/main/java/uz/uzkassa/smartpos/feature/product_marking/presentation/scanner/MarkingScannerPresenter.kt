package uz.uzkassa.smartpos.feature.product_marking.presentation.scanner

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.product_marking.data.exception.DuplicateProductMarkingException
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureCallback
import uz.uzkassa.smartpos.feature.product_marking.domain.ProductMarkingInteractor
import javax.inject.Inject

internal class MarkingScannerPresenter @Inject constructor(
    private val barcodeScannerManager: BarcodeScannerManager,
    private val productMarkingInteractor: ProductMarkingInteractor,
    private val productMarkingFeatureCallback: ProductMarkingFeatureCallback
) : MvpPresenter<MarkingScannerView>() {

    override fun onFirstViewAttach() {
        viewState.setProductCount(
            totalQuantity = productMarkingInteractor.getTotalProductQuantity(),
            markedQuantity = productMarkingInteractor.getMarkedProductQuantity()
        )
        getScannerResult()
    }

    @FlowPreview
    private fun getScannerResult() {
        barcodeScannerManager
            .getResult()
            .onEach {
                productMarkingInteractor.setMarkingForProduct(it)
                    .launchCatchingIn(presenterScope)
                    .onSuccess {
                        val marked = productMarkingInteractor.getMarkedProductQuantity()
                        viewState.setProductCount(
                            totalQuantity = productMarkingInteractor.getTotalProductQuantity(),
                            markedQuantity = marked
                        )
                        if (marked == productMarkingInteractor.getTotalProductQuantity()){
                            productMarkingFeatureCallback.onFinish(productMarkingInteractor.getProductMarkingResult())
                            viewState.onDismissView()
                        }
                    }
                    .onFailure { throwable ->
                        if (throwable is DuplicateProductMarkingException) viewState.onDuplicateError(
                            throwable
                        )
                        else viewState.onUndefinedError()
                    }
            }
            .launchIn(presenterScope)
    }

    fun setBarcodeResult(params: BarcodeScannerParams?) {
        if (params == null) return
        barcodeScannerManager.setBarcodeScannerParams(params)
    }

    fun onOpenCameraScanner() {
        viewState.onDismissView()
        productMarkingFeatureCallback.onOpenCameraScanner(productMarkingInteractor.getMarkedMarkings())
    }

    fun onDismiss() {
        productMarkingFeatureCallback.onBack()
        viewState.onDismissView()
    }
}