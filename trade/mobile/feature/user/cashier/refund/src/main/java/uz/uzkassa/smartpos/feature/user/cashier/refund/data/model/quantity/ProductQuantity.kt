package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity

import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

data class ProductQuantity(
    val uid: Long?,
    val categoryId: Long?,
    val categoryName: String?,
    val productId: Long?,
    val productName: String,
    val productPrice: BigDecimal,
    val amount: BigDecimal,
    val price: BigDecimal,
    val vatRate: BigDecimal?,
    val quantity: Double,
    val maxQuantity :Double,
    val unit: Unit?,
    val lastUnitId: Long?,
    val barcode: String?,
    val markedMarkings : Array<String>?,
    val totalMarkings : Array<String>?
) {
    var difference = 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductQuantity

        if (uid != other.uid) return false
        if (categoryId != other.categoryId) return false
        if (categoryName != other.categoryName) return false
        if (productId != other.productId) return false
        if (amount != other.amount) return false
        if (price != other.price) return false
        if (productPrice != other.productPrice) return false
        if (vatRate != other.vatRate) return false
        if (quantity != other.quantity) return false
        if (lastUnitId != other.lastUnitId) return false
        if (unit != other.unit) return false
        if (barcode != other.barcode) return false
        if (productName != other.productName) return false
        if (markedMarkings != null) {
            if (other.markedMarkings == null) return false
            if (!markedMarkings.contentEquals(other.markedMarkings)) return false
        } else if (other.markedMarkings != null) return false
        if (totalMarkings != null) {
            if (other.totalMarkings == null) return false
            if (!totalMarkings.contentEquals(other.totalMarkings)) return false
        } else if (other.totalMarkings != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid?.hashCode() ?: 0
        result = 31 * result + (categoryId?.hashCode() ?: 0)
        result = 31 * result + (categoryName?.hashCode() ?: 0)
        result = 31 * result + (productId?.hashCode() ?: 0)
        result = 31 * result + amount.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + (vatRate?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        result = 31 * result + (lastUnitId?.hashCode() ?: 0)
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + (barcode?.hashCode() ?: 0)
        result = 31 * result + productName.hashCode()
        result = 31 * result + (markedMarkings?.contentHashCode() ?: 0)
        result = 31 * result + (totalMarkings?.contentHashCode() ?: 0)
        return result
    }
}