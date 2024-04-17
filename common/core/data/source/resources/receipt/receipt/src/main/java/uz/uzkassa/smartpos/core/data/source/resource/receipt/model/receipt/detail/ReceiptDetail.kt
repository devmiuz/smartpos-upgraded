package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

data class ReceiptDetail(
    val categoryId: Long?,
    val categoryName: String?,
    val productId: Long?,
    val amount: BigDecimal,
    val discountAmount: BigDecimal,
    val discountPercent: Double,
    val exciseAmount: BigDecimal?,
    val exciseRateAmount: BigDecimal?,
    val marks: Array<String>?,
    val vatAmount: BigDecimal?,
    val vatRate: Double?,
    val quantity: Double,
    val price: BigDecimal,
    val status: ReceiptStatus,
    val unit: Unit?,
    val barcode: String?,
    val vatBarcode: String?,
    val name: String,
    val committentTin: String?,
    val vatPercent: Double?,
    val unitId: Long?,
    val label: String?
) {

    fun mapToFinalReceiptDetails(): ReceiptDetail {
        return ReceiptDetail(
            categoryId = categoryId,
            categoryName = categoryName,
            productId = productId,
            amount = amount,
            discountAmount = discountAmount,
            discountPercent = discountPercent,
            exciseAmount = exciseAmount,
            exciseRateAmount = exciseRateAmount,
            marks = marks,
            vatAmount = vatAmount,
            vatRate = vatRate,
            quantity = quantity,
            price = price,
            status = status,
            unit = unit,
            barcode = barcode,
            vatBarcode = vatBarcode,
            name = name,
            committentTin = committentTin,
            vatPercent = vatPercent,
            unitId = unitId,
            label = label
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReceiptDetail

        if (productId != other.productId) return false
        if (barcode != other.barcode) return false
        if (vatBarcode != other.vatBarcode) return false
        if (amount != other.amount) return false
        if (discountAmount != other.discountAmount) return false
        if (discountPercent != other.discountPercent) return false
        if (exciseAmount != other.exciseAmount) return false
        if (exciseRateAmount != other.exciseRateAmount) return false
        if (marks != null) {
            if (other.marks == null) return false
            if (!marks.contentEquals(other.marks)) return false
        } else if (other.marks != null) return false
        if (vatAmount != other.vatAmount) return false
        if (vatRate != other.vatRate) return false
        if (quantity != other.quantity) return false
        if (price != other.price) return false
        if (status != other.status) return false
        if (unit != other.unit) return false
        if (name != other.name) return false
        if (committentTin != other.committentTin) return false
        if (vatPercent != other.vatPercent) return false
        if (unitId != other.unitId) return false
        if (label != other.label) return false
        return true
    }

    override fun hashCode(): Int {
        var result = productId?.hashCode() ?: 0
        result = 31 * result + (barcode?.hashCode() ?: 0)
        result = 31 * result + (vatBarcode?.hashCode() ?: 0)
        result = 31 * result + amount.hashCode()
        result = 31 * result + discountAmount.hashCode()
        result = 31 * result + discountPercent.hashCode()
        result = 31 * result + (exciseAmount?.hashCode() ?: 0)
        result = 31 * result + (exciseRateAmount?.hashCode() ?: 0)
        result = 31 * result + (marks?.contentHashCode() ?: 0)
        result = 31 * result + (vatAmount?.hashCode() ?: 0)
        result = 31 * result + (vatRate?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + committentTin.hashCode()
        result = 31 * result + vatPercent.hashCode()
        result = 31 * result + unitId.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

}