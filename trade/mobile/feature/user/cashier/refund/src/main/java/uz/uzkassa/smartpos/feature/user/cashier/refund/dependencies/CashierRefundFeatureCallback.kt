package uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import java.math.BigDecimal

interface CashierRefundFeatureCallback {

    fun onOpenPaymentAmount(
        amount: BigDecimal,
        allowedAmount: BigDecimal,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        type: AmountType
    )

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
        maxQuantity: Double,
        quantity: Double,
        barcode: String?,
        productName: String,
        markedMarkings: Array<String>?,
        totalMarkings: Array<String>?
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
        markedMarkings: Array<String>,
        totalMarkings: Array<String>,
        isMarkingAvailable: Boolean,
        isCameraMode: Boolean
    )

    fun onOpenRequestSupervisorConfirmation(userRoleType: UserRole.Type)

    fun onBackFromCashierRefund()
}