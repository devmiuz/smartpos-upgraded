package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import java.math.BigDecimal
import java.util.*

data class Receipt constructor(
    val uid: String,
    val originUid: String?,
    val userId: Long,
    val customerId: Long?,
    val loyaltyCardId: Long?,
    val shiftId: Long?,
    val branchId: Long,
    val companyId: Long,
    val receiptDate: Date,
    val receiptLatitude: Double?,
    val receiptLongitude: Double?,
    val shiftNumber: Long?,
    val discountPercent: Double,
    val totalCard: BigDecimal,
    val totalCash: BigDecimal,
    val totalCost: BigDecimal,
    val totalDiscount: BigDecimal,
    val totalExcise: BigDecimal,
    val totalLoyaltyCard: BigDecimal,
    val totalVAT: BigDecimal,
    var totalPaid: BigDecimal,
    val terminalModel: String?,
    val terminalSerialNumber: String?,
    val fiscalSign: String?,
    val fiscalUrl: String?,
    val status: ReceiptStatus,
    val receiptDetails: List<ReceiptDetail>,
    val receiptPayments: List<ReceiptPayment>,
    val customerName: String?,
    val customerContact: String?,
    val readonly: Boolean?,
    val forceToPrint: Boolean?,
    val terminalId: String?,
    val receiptSeq: String?,
    val fiscalReceiptCreatedDate: Date?,
    val paymentBillId: String?,
    val baseStatus: ReceiptStatus,
    val transactionId: String?,
    val paymentProviderId: Int?
)