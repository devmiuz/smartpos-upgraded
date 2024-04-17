package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart

import android.util.Log
import dagger.Lazy
import kotlinx.coroutines.flow.*
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.cart.RefundCartInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter
import javax.inject.Inject
import kotlin.math.abs

internal class RefundCartPresenter @Inject constructor(
    private val refundRouter: RefundRouter,

    private val refundCartInteractor: RefundCartInteractor,

    private val refundCartLazyFlow: Lazy<Flow<RefundCart>>,
    private val productQuantityLazyFlow: Lazy<Flow<ProductQuantity>>,
    private val productMarkingLazyFlow: Lazy<Flow<ProductMarkingResult>>

) : MvpPresenter<RefundCartView>() {
    private var isToggleStateChanged: Boolean = false
    private var isToggleEnabled: Boolean = true
    private var isRefundAllowed: Boolean = false

    override fun onFirstViewAttach() {
        getProvidedProductQuantity()
        getProvidedRefundReceipt()
        getProvidedProductMarkingResult()
        getAvailableCash()
    }

    fun openProductQuantity(product: RefundCart.Product) {
        refundRouter.openProductQuantityScreen(product)
        viewState.onDismissRequestMarkingAlert()
    }

    fun openProductMarking(productQuantity: ProductQuantity) {
        refundRouter.openProductMarkingScreen(productQuantity)
        viewState.onDismissRequestMarkingAlert()
    }

    fun setToRefund(product: RefundCart.Product, enable: Boolean) {
        val isMarkingAvailable = !product.totalMarkings.isNullOrEmpty()
        if (isMarkingAvailable) {
            if (enable) viewState.onShowRequestMarkingAlert(product) else {
                refundCartInteractor.setToRefund(product, enable)
            }
        } else {
            refundCartInteractor.setToRefund(product, enable)
        }

        if (!enable) {
            isToggleEnabled = false
            isToggleStateChanged = true
            viewState.onToggleRefundProducts(isToggleEnabled)
        }
    }

    private fun getProvidedProductQuantity() {
        productQuantityLazyFlow.get()
            .onEach {
                val quantityDowngraded: Boolean = it.difference <= 0
                val difference = abs(it.difference)
                val isMarkingAvailable = !it.totalMarkings.isNullOrEmpty()

                if (isMarkingAvailable) {
                    refundCartInteractor.pendingUpdatedQuantity = it
                    if (quantityDowngraded) {
                        openProductMarking(
                            it.copy(
                                quantity = difference,
                                totalMarkings = if (it.markedMarkings.isNullOrEmpty()) it.totalMarkings else it.markedMarkings,
                                markedMarkings = arrayOf()
                            )
                        )
                    } else {
                        openProductMarking(
                            it.copy(quantity = it.difference)
                        )
                    }
                } else {
                    refundCartInteractor.updateQuantity(it)
                }

            }
            .launchIn(presenterScope)
    }

    private fun getProvidedProductMarkingResult() {
        productMarkingLazyFlow.get()
            .onEach {
                if (it.markings.isEmpty()) {
                    Log.e("enpremi", "marking skipped")
                    refundCartInteractor.uncheckProduct(it)
                } else {
                    refundCartInteractor.updateMarkings(it)
                }
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedRefundReceipt() {
        refundCartLazyFlow.get()
            .onEach {
                isRefundAllowed = it.isRefundAllowed
                viewState.onRefundReceiptDefined(it)
                if (!isToggleStateChanged && isToggleEnabled) {
                    isToggleEnabled = false
                    viewState.onToggleRefundProducts(isToggleEnabled)
                } else isToggleStateChanged = false
            }
            .launchIn(presenterScope)
    }

    fun toggleRefundProducts(enable: Boolean) {
        isToggleEnabled = enable
        isToggleStateChanged = true
        viewState.onToggleRefundProducts(isToggleEnabled)
        refundCartInteractor.toggleRefundProducts(isToggleEnabled)
    }

    fun dismissExitAlert(exit: Boolean) {
        if (exit) {
            viewState.onDismissExitAlert()
            isRefundAllowed = false
            backToRootScreen()
        } else viewState.onDismissExitAlert()
    }

    fun openPaymentScreen() =
        refundRouter.openPaymentScreen()

    fun backToRootScreen() {
        if (isRefundAllowed) viewState.onShowExitAlert()
        else refundRouter.backToReceiptSearchScreen()
    }

    private fun getAvailableCash() {
        refundCartInteractor
            .getAvailableCash()
            .launchCatchingIn(presenterScope)
            .onStart {}
            .onSuccess {
                refundCartInteractor.setAvailableCash(it)
            }
            .onFailure { viewState.onErrorAvailableCash(it) }
    }
}
