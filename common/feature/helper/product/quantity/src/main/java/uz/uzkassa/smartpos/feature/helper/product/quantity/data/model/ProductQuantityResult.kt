package uz.uzkassa.smartpos.feature.helper.product.quantity.data.model

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

data class ProductQuantityResult(
    val uid: Long?,
    val categoryId: Long?,
    val categoryName: String?,
    val productId: Long?,
    val amount: BigDecimal,
    val price: BigDecimal,
    val productPrice: BigDecimal,
    val vatRate: BigDecimal?,
    val quantity: Double,
    val lastUnitId: Long?,
    val unit: Unit?,
    val barcode: String?,
    val productName: String
) {

    internal constructor(
        uid: Long?,
        categoryId: Long?,
        categoryName: String?,
        productId: Long?,
        lastUnitId: Long?,
        amount: BigDecimal,
        price: BigDecimal,
        productPrice: BigDecimal,
        vatRate: BigDecimal?,
        barcode: String?,
        productName: String,
        productQuantity: ProductQuantity
    ) : this(
        uid = uid,
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        amount = amount,
        price = price,
        productPrice = productPrice,
        vatRate = vatRate,
        quantity = productQuantity.quantity,
        lastUnitId = lastUnitId,
        unit = productQuantity.productUnit?.unit,
        barcode = barcode,
        productName = productName
    )
}