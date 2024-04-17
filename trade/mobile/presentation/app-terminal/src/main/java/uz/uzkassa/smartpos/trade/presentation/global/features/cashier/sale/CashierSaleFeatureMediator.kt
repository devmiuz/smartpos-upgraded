package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.amount.AmountBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.bill.BillBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.discount.DiscountBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.product.ProductMarkingResultBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.product.ProductQuantityBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.CashierSaleFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.runner.CashierApayQRCodeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.runner.CashierCashOperationsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.runner.CashierReceiptRefundFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.mapper.map
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.runner.CashierSaleFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.runner.PaymentAmountFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.discount.runner.PaymentDiscountFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity.runner.ProductQuantityFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.runner.ProductMarkingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.runner.AutoPrintFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.runner.UserSettingsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountType as HelperDiscountType

class CashierSaleFeatureMediator(
    private val cashierApayQRCodeFeatureRunner: CashierApayQRCodeFeatureRunner,
    private val cashierCashOperationsFeatureRunner: CashierCashOperationsFeatureRunner,
    private val cashierReceiptRefundFeatureRunner: CashierReceiptRefundFeatureRunner,
    private val paymentAmountFeatureRunner: PaymentAmountFeatureRunner,
    private val paymentDiscountFeatureRunner: PaymentDiscountFeatureRunner,
    private val productQuantityFeatureRunner: ProductQuantityFeatureRunner,
    private val productMarkingFeatureRunner: ProductMarkingFeatureRunner,
    private val router: Router,
    private val userSettingsFeatureRunner: UserSettingsFeatureRunner,
    private val autoPrintFeatureRunner: AutoPrintFeatureRunner
) : FeatureMediator, CashierSaleFeatureArgs, CashierSaleFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var userRoleType: UserRole.Type by Delegates.notNull()
    override val amountBroadcastChannel = AmountBroadcastChannel()
    override val billBroadcastChannel = BillBroadcastChannel()
    override val discountBroadcastChannel = DiscountBroadcastChannel()
    override var branchId: Long by Delegates.notNull()
    override val productQuantityBroadcastChannel = ProductQuantityBroadcastChannel()
    override val productMarkingResultBroadcastChannel = ProductMarkingResultBroadcastChannel()
    override var userId: Long by Delegates.notNull()

    val featureRunner: CashierSaleFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenProductQuantity(
        uid: Long?,
        categoryId: Long?,
        categoryName: String?,
        productId: Long?,
        unitId: Long?,
        amount: BigDecimal,
        price: BigDecimal,
        productPrice: BigDecimal,
        vatRate: BigDecimal?,
        quantity: Double,
        maxQuantity: Double,
        productName: String,
        hasMark: Boolean,
        barcode: String?,
        markings: Array<String>
    ) {
        productQuantityFeatureRunner
            .finish {
                val productQuantity = it?.let {
                    ProductQuantity(
                        uid = uid,
                        categoryId = it.categoryId,
                        categoryName = it.categoryName,
                        productId = it.productId,
                        lastUnitId = it.lastUnitId,
                        amount = it.amount,
                        price = it.price,
                        productPrice = it.productPrice,
                        vatRate = vatRate,
                        quantity = it.quantity,
                        unit = it.unit,
                        barcode = it.barcode,
                        productName = productName,
                        markings = markings,
                        hasMark = hasMark
                    )
                }
                productQuantity?.difference =
                    if (it == null) 0.0 else it.quantity - quantity
                productQuantityBroadcastChannel.sendBlocking(productQuantity)
            }
            .run(
                uid = uid,
                categoryId = categoryId,
                categoryName = categoryName,
                productId = productId,
                unitId = unitId,
                amount = amount,
                price = price,
                productPrice = productPrice,
                vatRate = vatRate,
                quantity = quantity,
                maxQuantity = maxQuantity,
                barcode = barcode,
                productName = productName,
                isRefund = false
            ) {
                router.navigateTo(it)
            }
    }

    override fun onOpenProductMarking(
        categoryId: Long?,
        categoryName: String?,
        productId: Long?,
        quantity: Double,
        amount: BigDecimal,
        lastUnitId: Long?,
        price: BigDecimal,
        barcode: String?,
        productName: String,
        productPrice: BigDecimal,
        uid: Long?,
        unit: uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit?,
        vatRate: BigDecimal?,
        markings: Array<String>,
        totalMarkings: Array<String>,
        isMarkingAvailable: Boolean,
        isCameraMode: Boolean
    ) {
        productMarkingFeatureRunner
            .back { router.backTo(Screens.CashierSale) }
            .openCameraScanner { router.navigateTo(it) }
            .finish {
                router.backTo(Screens.CashierSale)
                productMarkingResultBroadcastChannel.sendBlocking(it)
            }
            .run(
                categoryId = categoryId,
                categoryName = categoryName,
                amount = amount,
                lastUnitId = lastUnitId,
                price = price,
                productId = productId,
                barcode = barcode,
                productName = productName,
                productPrice = productPrice,
                quantity = quantity,
                uid = uid,
                unit = unit,
                vatRate = vatRate,
                initialMarkings = markings,
                totalMarkings = totalMarkings,
                forRefund = false
            ) { scanner: Screen, cameraScanner: Screen ->
                when {
                    !isMarkingAvailable -> productMarkingResultBroadcastChannel.sendBlocking(
                        ProductMarkingResult(
                            categoryId = categoryId,
                            categoryName = categoryName,
                            amount = amount,
                            lastUnitId = lastUnitId,
                            price = price,
                            productId = productId,
                            barcode = barcode,
                            productName = productName,
                            productPrice = productPrice,
                            quantity = quantity,
                            uid = uid,
                            unit = unit,
                            vatRate = vatRate,
                            markings = markings
                        )
                    )
                    isCameraMode -> router.navigateTo(cameraScanner)
                    else -> router.navigateTo(scanner)
                }
            }
    }

    override fun onOpenPaymentAmount(
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        creditAdvanceHolder: CreditAdvanceHolder?
    ) {
        // TODO: 6/18/20 allowedAmount why nullable
        paymentAmountFeatureRunner
            .finish {
                amountBroadcastChannel.sendBlocking(it.map())
            }
            .run(
                creditAdvanceHolder,
                amount,
                if (type == ReceiptPayment.Type.CASH) null else leftAmount,
                type.map(),
                branchId,
                leftAmount,
                totalAmount
            ) {
                router.navigateTo(it)
            }
    }

    override fun onOpenApayScreen(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        description: String,
        uniqueId: String
    ) {
        cashierApayQRCodeFeatureRunner
            .back {
                router.backTo(Screens.CashierSale)
            }
            .finish { paymentAmount, billId ->
                billBroadcastChannel.sendBlocking(billId)
                amountBroadcastChannel.sendBlocking(paymentAmount)
                router.backTo(Screens.CashierSale)
            }
            .run(
                creditAdvanceHolder,
                amount,
                leftAmount,
                totalAmount,
                type,
                description,
                uniqueId
            ) {
                router.navigateTo(it)
            }
    }

    override fun onOpenUzCardScreen(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        description: String
    ) {
        cashierApayQRCodeFeatureRunner
            .back {
                router.backTo(Screens.CashierSale)
            }
            .finish { paymentAmount, billId ->
                billBroadcastChannel.sendBlocking(billId)
                amountBroadcastChannel.sendBlocking(paymentAmount)
                router.backTo(Screens.CashierSale)
            }
            // TODO: uniqueID
            .run(creditAdvanceHolder, amount, leftAmount, totalAmount, type, description, "") {
                router.navigateTo(it)
            }
    }

    override fun onOpenPaymentDiscount(
        amount: BigDecimal,
        discountAmount: BigDecimal,
        discountPercent: Double,
        discountType: DiscountType
    ) {
        paymentDiscountFeatureRunner
            .back { router.backTo(Screens.CashierSale) }
            .finish { discountBroadcastChannel.sendBlocking(it.map()) }
            .run(amount, discountAmount, discountPercent, discountType.map()) {
                router.navigateTo(it)
            }
    }

    override fun onBackFromSale() =
        backAction.invoke()

    override fun onOpenCashOperations() {
        cashierCashOperationsFeatureRunner
            .back { router.backTo(Screens.CashierSale) }
            .run(branchId, userId) { router.navigateTo(it) }
    }

    override fun onOpenRefundScreen() {
        cashierReceiptRefundFeatureRunner
            .back { router.backTo(Screens.CashierSale) }
            .run(branchId, userId, userRoleType) { router.navigateTo(it) }
    }

    override fun onOpenSettings(userId: Long) {
        userSettingsFeatureRunner
            .back { router.backTo(Screens.CashierSale) }
            .run(userId, userRoleType) { router.navigateTo(it) }
    }

    override fun onOpenAutoPrint(userId: Long) {
        autoPrintFeatureRunner
            .back { router.backTo(Screens.CashierSale) }
            .run(branchId, userId, userRoleType) { router.navigateTo(it) }
    }

    private inner class FeatureRunnerImpl : CashierSaleFeatureRunner {
        override fun run(
            branchId: Long,
            userId: Long,
            userRoleType: UserRole.Type,
            action: (Screen) -> Unit
        ) {
            this@CashierSaleFeatureMediator.branchId = branchId
            this@CashierSaleFeatureMediator.userId = userId
            this@CashierSaleFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.CashierSale)
        }

        override fun back(action: () -> Unit): CashierSaleFeatureRunner {
            backAction = action
            return this
        }
    }

    private fun DiscountType.map(): HelperDiscountType = when (this) {
        DiscountType.BY_SUM -> HelperDiscountType.BY_SUM
        DiscountType.BY_PERCENT -> HelperDiscountType.BY_PERCENT
    }

    private object Screens {

        object CashierSale : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CashierSaleFragment.newInstance()
        }
    }
}