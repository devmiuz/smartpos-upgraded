package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "receipt_details")
data class ReceiptDetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receipt_detail_id")
    val id: Long,

    @ColumnInfo(name = "receipt_detail_receipt_uid")
    val receiptUid: String,

    @ColumnInfo(name = "receipt_detail_category_id")
    val categoryId: Long?,

    @ColumnInfo(name = "receipt_detail_category_name")
    val categoryName: String?,

    @ColumnInfo(name = "receipt_detail_product_id")
    val productId: Long?,

    @ColumnInfo(name = "receipt_detail_product_package_type_id")
    val packageTypeId: Long?,

    @ColumnInfo(name = "receipt_detail_unit_id")
    val unitId: Long?,

    @ColumnInfo(name = "receipt_detail_unit_name")
    val unitName: String?,

    @ColumnInfo(name = "receipt_detail_amount")
    val amount: BigDecimal,

    @ColumnInfo(name = "receipt_detail_discount")
    val discountAmount: BigDecimal,

    @ColumnInfo(name = "receipt_detail_discount_percent")
    val discountPercent: Double,

    @ColumnInfo(name = "receipt_detail_excise_amount")
    val exciseAmount: BigDecimal?,

    @ColumnInfo(name = "receipt_detail_excise_rate_amount")
    val exciseRateAmount: BigDecimal?,

    @ColumnInfo(name = "receipt_detail_marks")
    val marks: Array<String>?,

    @ColumnInfo(name = "receipt_detail_vat_amount")
    val vatAmount: BigDecimal?,

    @ColumnInfo(name = "receipt_detail_vat_percent")
    val vatPercent: Double?,

    @ColumnInfo(name = "receipt_detail_quantity")
    val quantity: Double,

    @ColumnInfo(name = "receipt_detail_price")
    val price: BigDecimal,

    @ColumnInfo(name = "receipt_detail_status")
    val status: String,

    @ColumnInfo(name = "receipt_detail_product_barcode")
    val barcode: String?,

    @ColumnInfo(name = "receipt_detail_product_vat_barcode")
    val vatBarcode: String?,

    @ColumnInfo(name = "receipt_detail_product_name")
    val name: String,

    @ColumnInfo(name = "receipt_detail_product_committent_tin")
    val committentTin: String? = null,

    @ColumnInfo(name = "receipt_detail_product_label")
    val label: String? = null
) {

    @Ignore
    constructor(
        receiptUid: String,
        categoryId: Long?,
        categoryName: String?,
        productId: Long?,
        unitId: Long?,
        unitName: String?,
        amount: BigDecimal,
        discountAmount: BigDecimal,
        discountPercent: Double,
        exciseAmount: BigDecimal?,
        exciseRateAmount: BigDecimal?,
        marks: Array<String>?,
        vatAmount: BigDecimal?,
        vatPercent: Double?,
        quantity: Double,
        price: BigDecimal,
        status: String,
        barcode: String?,
        vatBarcode: String?,
        name: String,
        committentTin: String?,
        label: String?
    ) : this(
        id = 0,
        receiptUid = receiptUid,
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        packageTypeId = null,
        unitId = unitId,
        unitName = unitName,
        amount = amount,
        marks = marks,
        discountAmount = discountAmount,
        discountPercent = discountPercent,
        exciseAmount = exciseAmount,
        exciseRateAmount = exciseRateAmount,
        vatAmount = vatAmount,
        vatPercent = vatPercent,
        quantity = quantity,
        price = price,
        status = status,
        barcode = barcode,
        name = name,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        label = label
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReceiptDetailEntity

        if (id != other.id) return false
        if (receiptUid != other.receiptUid) return false
        if (productId != other.productId) return false
        if (packageTypeId != other.packageTypeId) return false
        if (unitId != other.unitId) return false
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
        if (vatPercent != other.vatPercent) return false
        if (quantity != other.quantity) return false
        if (price != other.price) return false
        if (status != other.status) return false
        if (name != other.name) return false
        if (committentTin != other.committentTin) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + receiptUid.hashCode()
        result = 31 * result + (productId?.hashCode() ?: 0)
        result = 31 * result + (packageTypeId?.hashCode() ?: 0)
        result = 31 * result + (unitId?.hashCode() ?: 0)
        result = 31 * result + amount.hashCode()
        result = 31 * result + discountAmount.hashCode()
        result = 31 * result + discountPercent.hashCode()
        result = 31 * result + (exciseAmount?.hashCode() ?: 0)
        result = 31 * result + (exciseRateAmount?.hashCode() ?: 0)
        result = 31 * result + (marks?.contentHashCode() ?: 0)
        result = 31 * result + (vatAmount?.hashCode() ?: 0)
        result = 31 * result + (vatPercent?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + committentTin.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }


}