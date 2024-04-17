package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction

import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import java.math.BigDecimal
import java.util.*

data class CashTransaction(
    val receiptUid: String,
    val userId: Long,
    val shiftId: Long?,
    val branchId: Long,
    val companyId: Long,
    val receiptDate: Date,
    val shiftNumber: Long,
    val totalAmount: BigDecimal,
    val totalCash: BigDecimal,
    val terminalModel: String?,
    val terminalSerialNumber: String?,
    val operation: CashOperation,
    val message: String,
    val isSynced: Boolean
)