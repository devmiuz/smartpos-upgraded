package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "receipt_payments")
data class ReceiptPaymentEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receipt_payment_id")
    val id: Long,

    @ColumnInfo(name = "receipt_payment_receipt_uid")
    val receiptUid: String,

    @ColumnInfo(name = "receipt_payment_amount")
    val amount: BigDecimal,

    @ColumnInfo(name = "receipt_payment_type")
    val type: String
)