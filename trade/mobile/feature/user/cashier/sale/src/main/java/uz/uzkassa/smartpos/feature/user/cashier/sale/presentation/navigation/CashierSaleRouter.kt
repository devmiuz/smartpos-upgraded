package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntent
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.scanner.CameraScannerFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.ProductSelectionFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.CreditAdvanceListFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.scanner.ReceiptQrCameraScannerFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.ReceiptDraftListFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.SalePaymentFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.PaymentProvidersFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.TabFragment
import java.math.BigDecimal

internal class CashierSaleRouter(
    private val cashierSaleFeatureArgs: CashierSaleFeatureArgs,
    private val cashierSaleFeatureCallback: CashierSaleFeatureCallback,
    private val gtposLaunchIntent: GTPOSLaunchIntent,
    private val router: Router
) {
    var onCameraScannerMode = false

    fun openCameraScannerScreen() {
        onCameraScannerMode = true
        router.navigateTo(Screens.CameraScannerScreen)
    }

    fun openPaymentScreen() =
        router.navigateTo(Screens.PaymentScreen)

    fun openReceiptDraftListScreen() =
        router.navigateTo(Screens.ReceiptDraftListScreen)

    fun openCreditAdvanceReceiptListScreen() =
        router.navigateTo(Screens.CreditAdvanceReceiptListScreen)

    fun openProductSelectionScreen() =
        router.navigateTo(Screens.ProductSelectionScreen)

    fun openTabScreen() =
        router.newRootScreen(Screens.TabScreen)

    fun backToTabScreen() =
        router.backTo(Screens.TabScreen)

    fun backToPaymentScreen() = router.backTo(Screens.PaymentScreen)

    fun backToCreditAdvanceReceiptListScreen() =
        router.backTo(Screens.CreditAdvanceReceiptListScreen)

    fun openSettingsScreen() =
        cashierSaleFeatureCallback.onOpenSettings(cashierSaleFeatureArgs.userId)

    fun openAutoPrint() = cashierSaleFeatureCallback.onOpenAutoPrint(cashierSaleFeatureArgs.userId)

    fun openReceiptQrCameraScannerScreen() = router.navigateTo(Screens.ReceiptQrCameraScannerScreen)

    fun backToRootScreen() =
        cashierSaleFeatureCallback.onBackFromSale()

    fun openGTPOSScreenOrThrow() {
        val screen: Screen = object : SupportAppScreen() {
            override fun getActivityIntent(context: Context): Intent? =
                gtposLaunchIntent.intentOrThrow()
        }
        router.navigateTo(screen)
    }

    fun openProductMarkingScreen(
        productQuantity: ProductQuantity,
        markings: Array<String>,
        totalMarkings: Array<String>,
        isMarkingAvailable: Boolean
    ) {
        cashierSaleFeatureCallback.onOpenProductMarking(
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
            markings = markings,
            totalMarkings = totalMarkings,
            isMarkingAvailable = isMarkingAvailable,
            isCameraMode = onCameraScannerMode
        )
        onCameraScannerMode = false
    }

    fun openProductQuantityScreen(product: SaleCart.ItemType.Product) {
        cashierSaleFeatureCallback.onOpenProductQuantity(
            uid = product.uid,
            categoryId = product.categoryId,
            categoryName = product.categoryName,
            productId = product.productId,
            unitId = product.unit?.id,
            amount = product.price,
            price = product.price,
            productPrice = product.productPrice,
            vatRate = product.vatRate,
            quantity = product.quantity,
            maxQuantity = 0.0,
            productName = product.name,
            hasMark = product.hasMark,
            barcode = product.barcode,
            markings = product.markings
        )
    }

    fun openProductQuantityScreen(
        product: Product,
        markings: Array<String>? = null
    ) {
        cashierSaleFeatureCallback
            .onOpenProductQuantity(
                uid = null,
                categoryId = product.category?.id,
                categoryName = product.category?.name,
                productId = product.id,
                unitId = null,
                amount = product.salesPrice,
                price = product.salesPrice,
                productPrice = product.salesPrice,
                vatRate = product.vatRate,
                quantity = 0.0,
                maxQuantity = product.count,
                productName = product.name,
                hasMark = product.hasMark,
                barcode = product.barcode,
                markings = markings ?: arrayOf()
            )
    }

    fun openPaymentAmountScreen(
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        creditAdvanceHolder: CreditAdvanceHolder?
    ) {
        cashierSaleFeatureCallback.onOpenPaymentAmount(
            amount = amount,
            leftAmount = leftAmount,
            totalAmount = totalAmount,
            type = type,
            creditAdvanceHolder = creditAdvanceHolder
        )
    }

    fun openApayPaymentScreen(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        description: String,
        uniqueId : String
    ) {
        cashierSaleFeatureCallback.onOpenApayScreen(
            creditAdvanceHolder = creditAdvanceHolder,
            amount = amount,
            leftAmount = leftAmount,
            totalAmount = totalAmount,
            type = type,
            description = description,
            uniqueId = uniqueId
        )
    }

    fun openUzCardScreen(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        description: String
    ) {
        cashierSaleFeatureCallback.onOpenUzCardScreen(
            creditAdvanceHolder = creditAdvanceHolder,
            amount = amount,
            leftAmount = leftAmount,
            totalAmount = totalAmount,
            type = type,
            description = description
        )
    }


    fun openPaymentDiscountScreen(
        amount: BigDecimal,
        discountAmount: BigDecimal,
        discountPercent: Double,
        discountType: DiscountType
    ) {
        cashierSaleFeatureCallback.onOpenPaymentDiscount(
            amount = amount,
            discountAmount = discountAmount,
            discountPercent = discountPercent,
            discountType = discountType
        )
    }

    fun openCreditAdvanceReceiptDetailScreen() {
        TODO("Not yet implemented")
    }

    fun openPaymentProvidersScreen() {
        router.navigateTo(Screens.PaymentProvidersScreen)
    }

    private object Screens {

        object CameraScannerScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CameraScannerFragment.newInstance()
        }

        object PaymentScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                SalePaymentFragment.newInstance()
        }

        object ReceiptDraftListScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ReceiptDraftListFragment.newInstance()
        }

        object ProductSelectionScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ProductSelectionFragment.newInstance()
        }

        object TabScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                TabFragment.newInstance()
        }

        object CreditAdvanceReceiptListScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CreditAdvanceListFragment.newInstance()
        }

        object ReceiptQrCameraScannerScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ReceiptQrCameraScannerFragment.newInstance()
        }

        object PaymentProvidersScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                PaymentProvidersFragment.newInstance()
        }

    }
}