package uz.uzkassa.smartpos.feature.user.autoprint.data.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransaction
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import java.math.BigDecimal

internal fun Receipt.mapToCashTransaction(): CashTransaction =
    CashTransaction(
        receiptUid = uid,
        userId = userId,
        shiftId = shiftId,
        branchId = branchId,
        companyId = companyId,
        receiptDate = receiptDate,
        shiftNumber = checkNotNull(shiftNumber),
        totalAmount = BigDecimal.ZERO,
        totalCash = totalCash,
        terminalModel = terminalModel,
        terminalSerialNumber = terminalSerialNumber,
        operation = status.mapToOperation(),
        message = "",
        isSynced = false
    )

internal fun ReceiptStatus.mapToOperation(): CashOperation = when (this) {
    ReceiptStatus.PAID -> CashOperation.PAID
    ReceiptStatus.RETURNED -> CashOperation.RETURNED
    else -> CashOperation.UNKNOWN
}