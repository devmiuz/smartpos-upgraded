package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Deprecated("")
@Entity(tableName = "postpone_receipt_amounts")
data class PostponeReceiptAmountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "postpone_receipt_amount_uid")
    val uid: Long,

    @ColumnInfo(name = "postpone_receipt_amount_postpone_receipt_id")
    val receiptId: Long,

    @ColumnInfo(name = "postpone_receipt_amount_amount")
    val amount: BigDecimal,

    @ColumnInfo(name = "postpone_receipt_amount_type")
    val type: String
) {

    @Ignore
    constructor(
        receiptId: Long,
        amount: BigDecimal,
        type: String
    ) : this(0, receiptId, amount, type)
}