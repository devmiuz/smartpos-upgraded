package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

data class ProductQuantityResult(
    val uid: Long?,
    val productId: Long?,
    val lastUnitId: Long?,
    val amount: BigDecimal,
    val quantity: Double,
    val unit: Unit?,
    val productName: String
) {

    constructor(
        lastUnitId: Long?,
        productQuantity: ProductQuantity
    ) : this(
        uid = productQuantity.uid,
        productId = productQuantity.productId,
        lastUnitId = lastUnitId,
        amount = productQuantity.amount,
        quantity = productQuantity.quantity,
        unit = productQuantity.unit,
        productName = productQuantity.productName
    )
}