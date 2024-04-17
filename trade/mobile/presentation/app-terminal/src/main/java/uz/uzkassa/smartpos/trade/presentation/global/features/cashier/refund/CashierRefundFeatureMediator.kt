package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.amount.AmountBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.confirmation.SupervisorConfirmationBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.product.ProductMarkingResultBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.channel.product.ProductQuantityBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.confirmation.SupervisorConfirmationState
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureArgs
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.CashierRefundFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.mapper.map
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.runner.CashierReceiptRefundFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.runner.PaymentAmountFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity.runner.ProductQuantityFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.runner.ProductMarkingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.runner.UserConfirmationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.math.ceil
import kotlin.properties.Delegates

class CashierRefundFeatureMediator(
    private val paymentAmountFeatureRunner: PaymentAmountFeatureRunner,
    private val productQuantityFeatureRunner: ProductQuantityFeatureRunner,
    private val router: Router,
    private val userConfirmationFeatureRunner: UserConfirmationFeatureRunner,
    private val productMarkingFeatureRunner: ProductMarkingFeatureRunner
) : FeatureMediator, CashierRefundFeatureArgs, CashierRefundFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override val amountBroadcastChannel = AmountBroadcastChannel()
    override var branchId: Long by Delegates.notNull()
    override val supervisorConfirmationBroadcastChannel = SupervisorConfirmationBroadcastChannel()
    override val productQuantityBroadcastChannel = ProductQuantityBroadcastChannel()
    override val productMarkingResultBroadcastChannel = ProductMarkingResultBroadcastChannel()
    override var userId: Long by Delegates.notNull()
    override var userRoleType: UserRole.Type by Delegates.notNull()

    val featureRunner: CashierReceiptRefundFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenRequestSupervisorConfirmation(userRoleType: UserRole.Type) {
        userConfirmationFeatureRunner
            .finish {
                supervisorConfirmationBroadcastChannel
                    .sendBlocking(
                        if (it) SupervisorConfirmationState.CONFIRMED
                        else SupervisorConfirmationState.NOT_CONFIRMED
                    )
            }
            .run(branchId, userRoleType) {
                router.navigateTo(it)
            }
    }

    override fun onOpenPaymentAmount(
        amount: BigDecimal,
        allowedAmount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: AmountType
    ) {
        paymentAmountFeatureRunner
            .finish {
                amountBroadcastChannel.sendBlocking(it.map())
            }
            .run(null,amount, allowedAmount, type.map(), branchId, leftAmount, totalAmount) {
                router.navigateTo(it)
            }
    }

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
        maxQuantity: Double,
        quantity: Double,
        barcode: String?,
        productName: String,
        markedMarkings: Array<String>?,
        totalMarkings: Array<String>?
    ) {
        productQuantityFeatureRunner
            .finish {
                if (it == null) return@finish
                val productQuantity = it.let {
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
                        maxQuantity = maxQuantity,
                        unit = it.unit,
                        barcode = it.barcode,
                        productName = productName,
                        markedMarkings = markedMarkings,
                        totalMarkings = totalMarkings
                    )
                }
                productQuantity.difference = ceil(it.quantity) - ceil(quantity)
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
                isRefund = true
            ) { router.navigateTo(it) }
    }

    override fun onBackFromCashierRefund() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : CashierReceiptRefundFeatureRunner {

        override fun run(
            branchId: Long,
            userId: Long,
            userRoleType: UserRole.Type,
            action: (Screen) -> Unit
        ) {
            this@CashierRefundFeatureMediator.branchId = branchId
            this@CashierRefundFeatureMediator.userId = userId
            this@CashierRefundFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.ReceiptRefund)
        }

        override fun back(action: () -> Unit): CashierReceiptRefundFeatureRunner {
            backAction = action
            return this
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
        markedMarkings: Array<String>,
        totalMarkings: Array<String>,
        isMarkingAvailable: Boolean,
        isCameraMode: Boolean
    ) {
        productMarkingFeatureRunner
            .back {
                router.backTo(Screens.ReceiptRefund)
                productMarkingResultBroadcastChannel.sendBlocking(
                    ProductMarkingResult(
                        uid = uid,
                        categoryId = categoryId,
                        categoryName = categoryName,
                        productId = productId,
                        productName = productName,
                        productPrice = productPrice,
                        amount = amount,
                        price = price,
                        vatRate = vatRate,
                        quantity = quantity,
                        lastUnitId = lastUnitId,
                        unit = unit,
                        barcode = barcode,
                        markings = arrayOf()
                    )
                )
            }
            .openCameraScanner { router.navigateTo(it) }
            .finish {
                router.backTo(Screens.ReceiptRefund)
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
                initialMarkings = markedMarkings,
                totalMarkings = totalMarkings,
                forRefund = true
            ) { scanner: Screen, cameraScanner: Screen ->
                when {
                    !isMarkingAvailable ->
                        productMarkingResultBroadcastChannel.sendBlocking(
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
                                markings = totalMarkings
                            )
                        )
                    isCameraMode -> router.navigateTo(cameraScanner)
                    else -> router.navigateTo(scanner)
                }
            }
    }

    private object Screens {

        object ReceiptRefund : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CashierRefundFragment.newInstance()
        }
    }
}