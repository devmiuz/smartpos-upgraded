package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

/**
 * totalCash -> Сумма операция
 * totalAmount -> Текуший опщий сумма после операция
 * */

@Entity(tableName = "cash_transactions")
data class CashTransactionEntity(

    @PrimaryKey
    @ColumnInfo(name = "cash_transaction_receipt_uid")
    val receiptUid: String,

    @ColumnInfo(name = "cash_transaction_user_id")
    val userId: Long,

    @ColumnInfo(name = "cash_transaction_shift_id")
    val shiftId: Long?,

    @ColumnInfo(name = "cash_transaction_receipt_branch_id")
    val branchId: Long,

    @ColumnInfo(name = "cash_transaction_company_id")
    val companyId: Long,

    @ColumnInfo(name = "cash_transaction_receipt_date")
    val receiptDate: Date,

    @ColumnInfo(name = "cash_transaction_shift_number")
    val shiftNumber: Long,

    @ColumnInfo(name = "cash_transaction_total_cash")
    val totalCash: BigDecimal,

    @ColumnInfo(name = "cash_transaction_total_amount")
    val totalAmount: BigDecimal,

    @ColumnInfo(name = "cash_transaction_terminal_model")
    val terminalModel: String?,

    @ColumnInfo(name = "cash_transaction_terminal_serial_number")
    val terminalSerialNumber: String?,

    @ColumnInfo(name = "cash_transaction_operation")
    val operation: String,

    @ColumnInfo(name = "cash_transaction_message")
    val message: String,

    @ColumnInfo(name = "cash_transaction_is_sync")
    val isSynced: Boolean
)