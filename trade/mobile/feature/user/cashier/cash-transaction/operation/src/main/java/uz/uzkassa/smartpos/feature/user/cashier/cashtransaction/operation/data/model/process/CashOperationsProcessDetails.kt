package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.process

import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import java.math.BigDecimal

internal data class CashOperationsProcessDetails(
    val allowedAmount: BigDecimal,
    val cashAmount: BigDecimal,
    val cashOperation: CashOperation
)