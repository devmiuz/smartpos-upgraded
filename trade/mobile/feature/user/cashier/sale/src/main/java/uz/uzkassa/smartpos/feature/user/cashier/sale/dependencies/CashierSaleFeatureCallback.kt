package uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import java.math.BigDecimal

interface CashierSaleFeatureCallback {

    fun onOpenProductQuantity(
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
    )

    fun onOpenProductMarking(
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
        unit: Unit?,
        vatRate: BigDecimal?,
        markings: Array<String>,
        totalMarkings: Array<String>,
        isMarkingAvailable: Boolean,
        isCameraMode: Boolean
    )

    fun onBackFromSale()

    fun onOpenCashOperations()

    fun onOpenRefundScreen()

    fun onOpenSettings(userId: Long)

    fun onOpenAutoPrint(userId: Long)

    fun onOpenPaymentAmount(
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        creditAdvanceHolder: CreditAdvanceHolder?
    )

    fun onOpenApayScreen(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        description: String,
        uniqueId : String
    )

    fun onOpenUzCardScreen(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: ReceiptPayment.Type,
        description: String
    )

    fun onOpenPaymentDiscount(
        amount: BigDecimal,
        discountAmount: BigDecimal,
        discountPercent: Double,
        discountType: DiscountType
    )
}