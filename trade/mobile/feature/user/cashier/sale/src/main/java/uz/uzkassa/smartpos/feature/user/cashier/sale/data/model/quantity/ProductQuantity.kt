package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

data class ProductQuantity(
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
    val productName: String,
    val hasMark: Boolean,
    val markings: Array<String> = arrayOf()
) {

    var difference = 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductQuantity

        if (uid != other.uid) return false
        if (productId != other.productId) return false
        if (amount != other.amount) return false
        if (price != other.price) return false
        if (productPrice != other.productPrice) return false
        if (vatRate != other.vatRate) return false
        if (quantity != other.quantity) return false
        if (hasMark != other.hasMark) return false
        if (lastUnitId != other.lastUnitId) return false
        if (unit != other.unit) return false
        if (productName != other.productName) return false
        if (!markings.contentEquals(other.markings)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid?.hashCode() ?: 0
        result = 31 * result + (productId?.hashCode() ?: 0)
        result = 31 * result + amount.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + (vatRate?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        result = 31 * result + hasMark.hashCode()
        result = 31 * result + (lastUnitId?.hashCode() ?: 0)
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + productName.hashCode()
        result = 31 * result + markings.contentHashCode()
        return result
    }
}