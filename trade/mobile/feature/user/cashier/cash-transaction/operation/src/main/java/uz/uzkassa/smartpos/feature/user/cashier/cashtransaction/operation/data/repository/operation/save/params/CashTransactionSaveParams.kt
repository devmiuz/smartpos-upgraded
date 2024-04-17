package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.params

import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransaction
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import java.math.BigDecimal
import java.util.*
import kotlin.properties.Delegates

internal data class CashTransactionSaveParams(
    val allowedAmount: BigDecimal,
    val cashAmount: CashAmount,
    val cashOperation: CashOperation,
    val message: String,
    val totalCash: BigDecimal
) {
    private val cashTransactionDate: Date = Date()
    private val uid: String by lazy { userId.toString() + cashTransactionDate.time }
    private var userId: Long by Delegates.notNull()

    fun asCashTransaction(
        userId: Long,
        shiftId: Long,
        branchId: Long,
        companyId: Long,
        shiftNumber: Int?,
        terminalModel: String,
        terminalSerialNumber: String
    ): CashTransaction {
        this.userId = userId
        return CashTransaction(
            receiptUid = uid,
            userId = userId,
            shiftId = shiftId,
            branchId = branchId,
            companyId = companyId,
            receiptDate = cashTransactionDate,
            shiftNumber = checkNotNull(shiftNumber).toLong(),
            totalCash = totalCash,
            totalAmount = BigDecimal.ZERO,
            terminalModel = terminalModel,
            terminalSerialNumber = terminalSerialNumber,
            operation = cashOperation,
            message = message,
            isSynced = false
        )
    }

}