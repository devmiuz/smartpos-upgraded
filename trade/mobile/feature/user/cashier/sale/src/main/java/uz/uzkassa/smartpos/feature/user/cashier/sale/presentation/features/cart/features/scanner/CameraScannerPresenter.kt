package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.camera.presentation.delegate.type.CameraType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.product.ProductNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.ProductByBarcodeInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import javax.inject.Inject

internal class CameraScannerPresenter @Inject constructor(
    private val productByBarcodeInteractor: ProductByBarcodeInteractor,
    private val cashierSaleRouter: CashierSaleRouter,
    private val unitLazyFlow: Lazy<Flow<Unit?>>
) : MvpPresenter<CameraScannerView>() {

    override fun onFirstViewAttach() =
        getProvidedUnit()

    val markings = mutableListOf<String>()

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun handleResult(result: String) {
        productByBarcodeInteractor
            .getProductByBarcode(readBarcodeFromMarking(result))
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onPauseCamera()
                viewState.onLoadingProduct()
            }
            .onSuccess {
                viewState.onSuccessProduct()
                cashierSaleRouter.backToTabScreen()
                cashierSaleRouter.openProductQuantityScreen(it, markings.toTypedArray())
            }
            .onFailure {
                when (it) {
                    is ProductNotFoundException ->
                        viewState.onErrorProductCauseNotFound()
                    else -> viewState.onErrorProduct(it)
                }
                viewState.onResumeCamera()
            }
    }

    private fun readBarcodeFromMarking(rawMarking: String): String {
        var marking = rawMarking
        println("initialLength ${marking.length}")
        if (rawMarking.length == 29) {
            marking = rawMarking.substring(0, 21)
            markings.add(marking)

            var gtinStartIndex = rawMarking.indexOf("21")
            println("initial lastIndex1 $gtinStartIndex")
            if (gtinStartIndex == -1) return marking
            val gtinMaxLength = 16
            gtinStartIndex =
                if (gtinStartIndex - gtinMaxLength > 0) gtinMaxLength else gtinStartIndex
            println("actual lastIndex1 $gtinStartIndex")

            marking = rawMarking.substring(rawMarking.indexOf("010") + 3, gtinStartIndex+1)

        } else if (rawMarking.length > 29) {
            val startIndex1 = 2

            var gtinStartIndex = rawMarking.indexOf("21")
            println("initial lastIndex1 $gtinStartIndex")
            if (gtinStartIndex == -1) return marking
            val gtinMaxLength = 16
            gtinStartIndex =
                if (gtinStartIndex - gtinMaxLength > 0) gtinMaxLength else gtinStartIndex
            println("actual lastIndex1 $gtinStartIndex")
            marking = rawMarking.substring(startIndex1, gtinStartIndex)
            println("firstMarking $marking")

            val startIndex2 = rawMarking.indexOf("21") + 2
            println("startIndex2 $startIndex2")

            var unprintableSignIndex = rawMarking.indexOf("\u001D")
            println("initial lastIndex2 $unprintableSignIndex")
            unprintableSignIndex =
                if (unprintableSignIndex == -1 || unprintableSignIndex < startIndex2) {
                    val ciiIndex = checkCIICode(rawMarking, startIndex2)

                    val serialNumberMaxLength = 22

                    if (ciiIndex != -1) {
                        ciiIndex
                    } else {
                        val maxLastIndex = startIndex2 + serialNumberMaxLength
                        if (rawMarking.length > maxLastIndex) maxLastIndex else rawMarking.length
                    }

                } else {
                    unprintableSignIndex
                }
            println("actual lastIndex2 $unprintableSignIndex")

            println("secondMarking ${rawMarking.substring(startIndex2, unprintableSignIndex)}")
            marking += rawMarking.substring(startIndex2, unprintableSignIndex)
            markings.add(marking)

            marking = rawMarking.substring(rawMarking.indexOf("010") + 3, gtinStartIndex+1)
        }
        println("result marking $marking")
        println("markingResultLength ${marking.length}")
        return marking
    }

    fun checkCIICode(rawMarking: String, startIndex: Int): Int {
        println("----------->")
        var codeFoundIndex = -1
        for (code in (91..99)) {
            println("code : $code")
            val index = rawMarking.indexOf(code.toString(), startIndex, true)
            println("index : $index")
            if (index != -1) {
                codeFoundIndex = index
                break
            }
        }
        println("<------------")
        return codeFoundIndex
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

    fun backToRootScreen() {
        cashierSaleRouter.onCameraScannerMode = false
        cashierSaleRouter.backToTabScreen()
    }

    private fun getProvidedUnit() {
        unitLazyFlow.get()
            .onEach { viewState.onResumeCamera() }
            .launchIn(presenterScope)
    }
}