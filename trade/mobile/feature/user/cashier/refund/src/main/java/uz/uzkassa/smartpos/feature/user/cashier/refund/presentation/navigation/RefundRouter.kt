package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart.RefundCartFragment
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.RefundPaymentFragment
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.RefundReceiptSearchFragment
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.scanner.ReceiptQrCameraScannerFragment
import java.math.BigDecimal

internal class RefundRouter(
    private val cashierRefundFeatureArgs: CashierRefundFeatureArgs,
    private val cashierRefundFeatureCallback: CashierRefundFeatureCallback,
    private val router: Router
) {

    fun openReceiptSearchScreen() =
        router.newRootScreen(Screens.ReceiptSearchScreen)

    fun backToReceiptSearchScreen() =
        router.backTo(Screens.ReceiptSearchScreen)

    fun openReceiptQrCameraScannerScreen() =
        router.navigateTo(Screens.ReceiptQrCameraScannerScreen)

    fun openProductListScreen() =
        router.navigateTo(Screens.ProductListScreen)

    fun backToProductListScreen() =
        router.backTo(Screens.ProductListScreen)

    fun openPaymentScreen() =
        router.navigateTo(Screens.PaymentScreen)

    fun openProductQuantityScreen(product: RefundCart.Product) {
        cashierRefundFeatureCallback.onOpenProductQuantity(
            uid = product.uid,
            categoryId = product.categoryId,
            categoryName = product.categoryName,
            productId = product.productId,
            unitId = product.detailUnit?.id,
            amount = product.amount,
            price = product.price,
            productPrice = product.productPrice,
            vatRate = product.vatRate,
            quantity = product.quantity,
            maxQuantity = product.detailQuantity,
            barcode = product.barcode,
            productName = product.name,
            markedMarkings = product.markedMarkings,
            totalMarkings = product.totalMarkings
        )
    }

    fun openPaymentAmountScreen(
        amount: BigDecimal,
        allowedAmount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: AmountType
    ) {
        cashierRefundFeatureCallback.onOpenPaymentAmount(
            amount = amount,
            allowedAmount = allowedAmount,
            leftAmount = leftAmount,
            totalAmount = totalAmount,
            type = type
        )
    }

    fun openSupervisorConfirmationScreen() =
        cashierRefundFeatureCallback
            .onOpenRequestSupervisorConfirmation(cashierRefundFeatureArgs.userRoleType)

    fun exit() =
        cashierRefundFeatureCallback.onBackFromCashierRefund()

    fun openProductMarkingScreen(
        productQuantity: ProductQuantity
    ) {
        cashierRefundFeatureCallback.onOpenProductMarking(
            categoryId = productQuantity.categoryId,
            categoryName = productQuantity.categoryName,
            amount = productQuantity.amount,
            lastUnitId = productQuantity.lastUnitId,
            price = productQuantity.price,
            productId = productQuantity.productId,
            productName = productQuantity.productName,
            barcode = productQuantity.barcode,
            productPrice = productQuantity.productPrice,
            quantity = productQuantity.quantity,
            uid = productQuantity.uid,
            unit = productQuantity.unit,
            vatRate = productQuantity.vatRate,
            markedMarkings = productQuantity.markedMarkings ?: arrayOf(),
            totalMarkings = productQuantity.totalMarkings ?: arrayOf(),
            isMarkingAvailable = !productQuantity.totalMarkings.isNullOrEmpty(),
            isCameraMode = false
        )
//        onCameraScannerMode = false
    }

    private object Screens {

        object PaymentScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                RefundPaymentFragment.newInstance()
        }

        object ProductListScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                RefundCartFragment.newInstance()
        }

        object ReceiptQrCameraScannerScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ReceiptQrCameraScannerFragment.newInstance()
        }

        object ReceiptSearchScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                RefundReceiptSearchFragment.newInstance()
        }
    }
}