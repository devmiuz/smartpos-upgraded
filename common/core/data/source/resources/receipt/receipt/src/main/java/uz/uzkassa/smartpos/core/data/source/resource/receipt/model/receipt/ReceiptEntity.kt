package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "receipts")
data class ReceiptEntity(

    @PrimaryKey
    @ColumnInfo(name = "receipt_uid")
    val uid: String,

    @ColumnInfo(name = "receipt_origin_uid")
    val originUid: String?,

    @ColumnInfo(name = "receipt_user_id")
    val userId: Long,

    @ColumnInfo(name = "receipt_customer_id")
    val customerId: Long?,

    @ColumnInfo(name = "receipt_loyalty_card_id")
    val loyaltyCardId: Long?,

    @ColumnInfo(name = "receipt_shift_id")
    val shiftId: Long?,

    @ColumnInfo(name = "receipt_branch_id")
    val branchId: Long,

    @ColumnInfo(name = "receipt_company_id")
    val companyId: Long,

    @ColumnInfo(name = "receipt_date")
    val receiptDate: Date,

    @ColumnInfo(name = "receipt_latitude")
    val receiptLatitude: Double?,

    @ColumnInfo(name = "receipt_longitude")
    val receiptLongitude: Double?,

    @ColumnInfo(name = "receipt_shift_no")
    val shiftNumber: Long?,

    @ColumnInfo(name = "receipt_discount_percent")
    val discountPercent: Double,

    @ColumnInfo(name = "receipt_total_card")
    val totalCard: BigDecimal,

    @ColumnInfo(name = "receipt_total_cash")
    val totalCash: BigDecimal,

    @ColumnInfo(name = "receipt_total_cost")
    val totalCost: BigDecimal,

    @ColumnInfo(name = "receipt_total_discount")
    val totalDiscount: BigDecimal,

    @ColumnInfo(name = "receipt_total_excise")
    val totalExcise: BigDecimal,

    @ColumnInfo(name = "receipt_total_loyalty_card")
    val totalLoyaltyCard: BigDecimal,

    @ColumnInfo(name = "receipt_total_vat")
    val totalVAT: BigDecimal,

    @ColumnInfo(name = "receipt_total_paid")
    val totalPaid: BigDecimal,

    @ColumnInfo(name = "receipt_terminal_model")
    val terminalModel: String?,

    @ColumnInfo(name = "receipt_terminal_serial_number")
    val terminalSerialNumber: String?,

    @ColumnInfo(name = "receipt_fiscal_sign")
    val fiscalSign: String?,

    @ColumnInfo(name = "receipt_fiscal_url")
    val fiscalUrl: String?,

    @ColumnInfo(name = "receipt_status_receipt_status")
    val status: String,

    @ColumnInfo(name = "receipt_customer_name")
    val customerName: String?,

    @ColumnInfo(name = "receipt_customer_contact")
    val customerContact: String?,

    @ColumnInfo(name = "receipt_readonly")
    val readonly: Boolean?,

    @ColumnInfo(name = "receipt_force_to_print")
    val forceToPrint: Boolean?,

    @ColumnInfo(name = "receipt_is_synced")
    val isSynced: Boolean,

    @ColumnInfo(name = "receipt_terminal_id")
    val terminalId: String?,

    @ColumnInfo(name = "receipt_seq")
    val receiptSeq: String?,

    @ColumnInfo(name = "fiscal_receipt_created_date")
    val fiscalReceiptCreatedDate: Date?,

    @ColumnInfo(name = "payment_bill_id")
    val paymentBillId: String?,

    @ColumnInfo(name = "receipt_base_status")
    val baseStatus: String,

    @ColumnInfo(name = "transaction_id")
    val transactionId: String?,

    @ColumnInfo(name = "payment_provider_id")
    val paymentProviderId: Int?
)