package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart

import android.util.Log
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.core.utils.math.multiply
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart.ItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.cart.SaleCartInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.floor

internal class SaleCartPresenter @Inject constructor(
    private val barcodeScannerManager: BarcodeScannerManager,
    private val cashierSaleRouter: CashierSaleRouter,
    private val drawerStateDelegate: DrawerStateDelegate,
    private val productResultLazyFlow: Lazy<Flow<Result<Product>>>,
    private val productsLazyFlow: Lazy<Flow<List<ItemType.Product>>>,
    private val receiptHeldLazyFlow: Lazy<Flow<Unit>>,
    private val saleCartInteractor: SaleCartInteractor,
    private val productMarkingInteractor: ProductMarkingInteractor,
    private val saleCartLazyFlow: Lazy<Flow<SaleCart>>,
    private val productQuantityLazyFlow: Lazy<Flow<ProductQuantity>>,
    private val saleProductQuantityLazyFlow: Lazy<Flow<SaleProductQuantity>>
) : MvpPresenter<SaleCartView>() {
    private var isResumed: Boolean = false
    private var barcodeScannerUsed: Boolean = false

    override fun onFirstViewAttach() {
        getBarcodeScannerResult()
        getProvidedProductCount()
        getProvidedProducts()
        getProvidedSaleCart()
        getReceiptHeld()
        getProvidedProductQuantity()
        viewState.onCartDefined(SaleCart(emptyList()))
    }

    fun setToResume(value: Boolean) {
        isResumed = value
    }

    fun openProductSelectionScreen() {
        cashierSaleRouter.openProductSelectionScreen()
    }

    fun openPaymentDetailsScreen() =
        cashierSaleRouter.openPaymentScreen()

    fun changeItemType(itemType: ItemType) {
        when (itemType) {
            is ItemType.FreePrice -> viewState.onShowFreePriceInput()
            is ItemType.Product -> cashierSaleRouter.openProductQuantityScreen(itemType)
        }
    }

    fun deleteItemType(itemType: ItemType) =
        saleCartInteractor.deleteItemType(itemType).let { isDone ->
            if (itemType is ItemType.Product)
                productMarkingInteractor.deleteLastItems(
                    markings = itemType.markings,
                    productId = itemType.productId,
                    count = itemType.markings.size
                ).launchIn(presenterScope)
            if (!isDone) viewState.onShowChangeNotAllowedAlert(true)
        }

    fun setBarcodeScannerParams(params: BarcodeScannerParams?) {
        if (params == null) return
        barcodeScannerUsed = true
        barcodeScannerManager.setBarcodeScannerParams(params)
    }

    fun toggleDrawer() {
        if (saleCartInteractor.isSaleAllowed()) drawerStateDelegate.toggle()
        else viewState.onShowFinishShiftAlert()
    }

    fun dismissChangeNotAllowedAlert() =
        viewState.onDismissChangeNotAllowedAlert()

    fun dismissFinishShiftAlert() =
        viewState.onDismissFinishShiftAlert()

    private fun getBarcodeScannerResult() {
        productResultLazyFlow.get()
            .launchCatchingIn(presenterScope)
            .onSuccess {
                if (barcodeScannerUsed)
                    cashierSaleRouter.openProductQuantityScreen(it)
                barcodeScannerUsed = false
            }
            .onFailure {
                if (barcodeScannerUsed)
                    viewState.onErrorProductBarcodeScan(it)
                barcodeScannerUsed = false
            }
    }

    private fun getProvidedProducts() {
        productsLazyFlow.get()
            .onEach { saleCartInteractor.setItemTypes(it) }
            .launchIn(presenterScope)
    }

    private fun getProvidedProductQuantity() {
        productQuantityLazyFlow.get()
            .onEach {
                var markedMarkings = it.markings

                if (it.markings.isEmpty()) {
                    markedMarkings = saleCartInteractor.getSelectedProductMarkedMarkings(it)
                }
                val quantityDowngraded: Boolean = it.difference < 0
                val isMarkingAvailable = it.difference != (0.0) && it.hasMark
                val difference = abs(it.difference).toInt()
                val flooredQuantity = floor(it.quantity)
                val rawAmount =
                    if (it.quantity - flooredQuantity > 0) (BigDecimal(flooredQuantity) * it.productPrice) else it.amount

                if (isMarkingAvailable) {
                    val amount = rawAmount
                        .divide(
                            BigDecimal(if (flooredQuantity == 0.0) 1.0 else flooredQuantity),
                            RoundingMode.FLOOR
                        )
                        .multiply(difference)
                    if (amount > BigDecimal.ZERO) {
                        if (quantityDowngraded) {
                            cashierSaleRouter.openProductMarkingScreen(
                                it.copy(
                                    quantity = it.difference,
                                    amount = amount
                                ),
                                arrayOf(),
                                markedMarkings,
                                isMarkingAvailable
                            )
                        } else {
                            cashierSaleRouter.openProductMarkingScreen(
                                it.copy(
                                    quantity = it.difference,
                                    amount = amount
                                ),
                                markedMarkings,
                                saleCartInteractor.getTotalMarkings(),
                                isMarkingAvailable
                            )
                        }
                    }
                } else {
                    cashierSaleRouter.openProductMarkingScreen(
                        it,
                        markedMarkings,
                        saleCartInteractor.getTotalMarkings(),
                        isMarkingAvailable
                    )
                }

            }
            .launchIn(presenterScope)
    }

    private fun getProvidedProductCount() {
        saleProductQuantityLazyFlow.get()
            .onEach {
                val isAllowed: Boolean =
                    if (it.markings.isEmpty()) {
                        if (isResumed)
                            saleCartInteractor.upsertProduct(it)
                        else
                            saleCartInteractor.updateProduct(it)
                    } else {
                        saleCartInteractor.updateProduct(it)
                    }

                if (!isAllowed) viewState.onShowChangeNotAllowedAlert(false)
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedSaleCart() {
        saleCartLazyFlow.get()
            .onEach { viewState.onCartDefined(it) }
            .launchIn(presenterScope)
    }

    private fun getReceiptHeld() {
        receiptHeldLazyFlow.get()
            .onEach { saleCartInteractor.clear() }
            .onEach {
                viewState.onCartDefined(SaleCart(emptyList()))
                viewState.onCleared()
            }
            .launchIn(presenterScope)
    }
}