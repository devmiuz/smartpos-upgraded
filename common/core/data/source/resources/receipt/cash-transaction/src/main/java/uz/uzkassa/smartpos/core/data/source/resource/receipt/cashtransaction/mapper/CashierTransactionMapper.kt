package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.status.CashTransactionStatusRequest
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransaction
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransactionEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransactionRequest
import java.math.BigDecimal

fun CashTransactionEntity.map(): CashTransaction =
    CashTransaction(
        receiptUid = receiptUid,
        userId = userId,
        shiftId = shiftId,
        branchId = branchId,
        companyId = companyId,
        receiptDate = receiptDate,
        shiftNumber = shiftNumber,
        totalAmount = totalAmount,
        totalCash = totalCash,
        terminalModel = terminalModel,
        terminalSerialNumber = terminalSerialNumber,
        operation = CashOperation.valueOf(operation),
        message = message,
        isSynced = isSynced
    )

fun CashTransaction.map(
    totalAmount: BigDecimal,
    shiftId: Long,
    shiftNumber: Long
): CashTransactionEntity =
    CashTransactionEntity(
        receiptUid = receiptUid,
        userId = userId,
        shiftId = shiftId,
        branchId = branchId,
        companyId = companyId,
        receiptDate = receiptDate,
        shiftNumber = shiftNumber,
        totalCash = totalCash,
        totalAmount = totalAmount,
        terminalModel = terminalModel,
        terminalSerialNumber = terminalSerialNumber,
        operation = operation.name,
        message = message,
        isSynced = isSynced
    )

fun List<CashTransaction>.mapToRequests() =
    map { it.mapToRequest() }

fun CashTransaction.mapToRequest(): CashTransactionRequest =
    CashTransactionRequest(
        receiptUid = receiptUid,
        userId = userId,
        shiftId = shiftId,
        branchId = branchId,
        companyId = companyId,
        receiptDate = receiptDate,
        shiftNumber = shiftNumber,
        totalCash = totalCash,
        totalCard = BigDecimal.ZERO,
        totalCost = totalAmount,
        totalPaid = totalAmount,
        terminalModel = terminalModel,
        discountPercent = 0.0,
        totalDiscount = BigDecimal.ZERO,
        totalVAT = BigDecimal.ZERO,
        terminalSerialNumber = terminalSerialNumber,
        status = operation.mapToStatusRequest()
    )

fun CashOperation.mapToStatusRequest(): CashTransactionStatusRequest = when (this) {
    CashOperation.PAID -> CashTransactionStatusRequest.PAID
    CashOperation.RETURNED -> CashTransactionStatusRequest.RETURNED
    CashOperation.INCOME -> CashTransactionStatusRequest.INCOME
    CashOperation.WITHDRAW -> CashTransactionStatusRequest.WITHDRAW
    CashOperation.FLOW -> CashTransactionStatusRequest.FLOW
    CashOperation.RETURN_FLOW -> CashTransactionStatusRequest.RETURN_FLOW
    CashOperation.INCASSATION -> CashTransactionStatusRequest.INCASSATION
//    CashOperation.ADVANCE -> CashTransactionStatusRequest.ADVANCE
//    CashOperation.CREDIT -> CashTransactionStatusRequest.CREDIT
    CashOperation.UNKNOWN -> CashTransactionStatusRequest.UNKNOWN
}