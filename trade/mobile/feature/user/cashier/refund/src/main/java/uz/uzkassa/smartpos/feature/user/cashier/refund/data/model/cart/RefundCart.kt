package uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.utils.math.sum
import java.math.BigDecimal

internal data class RefundCart(
    val receiptUid: String,
    val products: List<Product>,
    val shiftAvailableCash: BigDecimal
) {
    val amount: BigDecimal
        get() = products.map { it.amount }.sum()

    val isRefundAllowed: Boolean
        get() = amount > BigDecimal.ZERO && isCashAvailableForRefund

    val isCashAvailableForRefund: Boolean
//        get() = shiftAvailableCash >= amount
        get() = true

    data class Product(
        val categoryId: Long?,
        val categoryName: String?,
        val productId: Long?,
        val barcode: String?,
        val vatBarcode: String?,
        val detailAmount: BigDecimal,
        val amount: BigDecimal,
        val discountAmount: BigDecimal,
        val discountPercent: Double,
        val exciseAmount: BigDecimal?,
        val exciseRateAmount: BigDecimal?,
        val vatAmount: BigDecimal?,
        val vatRate: BigDecimal?,
        val detailQuantity: Double,
        val quantity: Double,
        val price: BigDecimal,
        val productPrice: BigDecimal,
        val status: ReceiptStatus,
        val detailUnit: Unit?,
        val unit: Unit?,
        val forRefund: Boolean,
        val name: String,
        val markedMarkings: Array<String>?,
        val totalMarkings: Array<String>?,
        val committentTin: String?,
        val vatPercent: Double?,
        val unitId: Long?,
        val label: String?
    ) {
        val uid: Long = System.currentTimeMillis()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Product

            if (categoryId != other.categoryId) return false
            if (categoryName != other.categoryName) return false
            if (productId != other.productId) return false
            if (barcode != other.barcode) return false
            if (vatBarcode != other.vatBarcode) return false
            if (detailAmount != other.detailAmount) return false
            if (amount != other.amount) return false
            if (discountAmount != other.discountAmount) return false
            if (discountPercent != other.discountPercent) return false
            if (exciseAmount != other.exciseAmount) return false
            if (exciseRateAmount != other.exciseRateAmount) return false
            if (vatAmount != other.vatAmount) return false
            if (vatRate != other.vatRate) return false
            if (detailQuantity != other.detailQuantity) return false
            if (quantity != other.quantity) return false
            if (price != other.price) return false
            if (productPrice != other.productPrice) return false
            if (status != other.status) return false
            if (detailUnit != other.detailUnit) return false
            if (unit != other.unit) return false
            if (forRefund != other.forRefund) return false
            if (name != other.name) return false
            if (markedMarkings != null) {
                if (other.markedMarkings == null) return false
                if (!markedMarkings.contentEquals(other.markedMarkings)) return false
            } else if (other.markedMarkings != null) return false
            if (totalMarkings != null) {
                if (other.totalMarkings == null) return false
                if (!totalMarkings.contentEquals(other.totalMarkings)) return false
            } else if (other.totalMarkings != null) return false
            if (uid != other.uid) return false
            if (committentTin != other.committentTin) return false
            if (vatPercent != other.vatPercent) return false
            if (unitId != other.unitId) return false
            if (label != other.label) return false
            return true
        }

        override fun hashCode(): Int {
            var result = categoryId?.hashCode() ?: 0
            result = 31 * result + (categoryName?.hashCode() ?: 0)
            result = 31 * result + (productId?.hashCode() ?: 0)
            result = 31 * result + (barcode?.hashCode() ?: 0)
            result = 31 * result + (vatBarcode?.hashCode() ?: 0)
            result = 31 * result + detailAmount.hashCode()
            result = 31 * result + amount.hashCode()
            result = 31 * result + discountAmount.hashCode()
            result = 31 * result + discountPercent.hashCode()
            result = 31 * result + (exciseAmount?.hashCode() ?: 0)
            result = 31 * result + (exciseRateAmount?.hashCode() ?: 0)
            result = 31 * result + (vatAmount?.hashCode() ?: 0)
            result = 31 * result + (vatRate?.hashCode() ?: 0)
            result = 31 * result + detailQuantity.hashCode()
            result = 31 * result + quantity.hashCode()
            result = 31 * result + price.hashCode()
            result = 31 * result + productPrice.hashCode()
            result = 31 * result + status.hashCode()
            result = 31 * result + (detailUnit?.hashCode() ?: 0)
            result = 31 * result + (unit?.hashCode() ?: 0)
            result = 31 * result + forRefund.hashCode()
            result = 31 * result + name.hashCode()
            result = 31 * result + (markedMarkings?.contentHashCode() ?: 0)
            result = 31 * result + (totalMarkings?.contentHashCode() ?: 0)
            result = 31 * result + uid.hashCode()
            result = 31 * result + committentTin.hashCode()
            result = 31 * result + vatPercent.hashCode()
            result = 31 * result + unitId.hashCode()
            result = 31 * result + label.hashCode()
            return result
        }


    }
}