package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import java.math.BigDecimal

data class CreditAdvanceHolder(
    var paymentAmount: BigDecimal = BigDecimal.ZERO,
    var status: ReceiptStatus = ReceiptStatus.CREDIT,
    var customerName: String = "",
    var customerPhone: String = "",
    val isRepayment: Boolean = false,
    val paidInFull: Boolean = false
)
