package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.transaction.TransactionHolder
import java.math.BigDecimal

data class Amount(
    val amount: BigDecimal,
    val changeAmount: BigDecimal,
    val leftAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val type: ReceiptPayment.Type,
    val creditAdvanceHolder: CreditAdvanceHolder? = null,
    var transactionHolder: TransactionHolder? = null
)