package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Deprecated("")
@Entity(tableName = "postpone_receipts")
data class PostponeReceiptEntity(
    @PrimaryKey
    @ColumnInfo(name = "postpone_receipt_id")
    val id: Long,

    @ColumnInfo(name = "postpone_receipt_uid")
    val uid: String?,

    @ColumnInfo(name = "postpone_receipt_date")
    val receiptDate: Date?,

    @ColumnInfo(name = "postpone_receipt_card_number")
    val cardNumber: String?,

    @ColumnInfo(name = "postpone_receipt_amount")
    val amount: BigDecimal,

    @ColumnInfo(name = "postpone_receipt_discount_arbitrary_amount")
    val discountAmount: BigDecimal?,

    @ColumnInfo(name = "postpone_receipt_discount_arbitrary_percent")
    val discountPercent: Double?,

    @ColumnInfo(name = "postpone_receipt_name")
    val name: String
)