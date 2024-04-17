package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Deprecated("")
@Entity(tableName = "postpone_receipt_details")
data class PostponeReceiptDetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "postpone_receipt_detail_uid")
    val uid: Long,

    @ColumnInfo(name = "postpone_receipt_detail_postpone_receipt_id")
    val receiptId: Long,

    @ColumnInfo(name = "postpone_receipt_detail_postpone_product_id")
    val productId: Long?,

    @ColumnInfo(name = "postpone_receipt_detail_postpone_product_unit_id")
    val unitId: Long?,

    @ColumnInfo(name = "postpone_receipt_detail_amount")
    val amount: BigDecimal,

    @ColumnInfo(name = "postpone_receipt_detail_count")
    val quantity: Double
) {

    @Ignore
    constructor(
        receiptId: Long,
        productId: Long?,
        unitId: Long?,
        amount: BigDecimal,
        quantity: Double
    ) : this(0, receiptId, productId, unitId, amount, quantity)
}