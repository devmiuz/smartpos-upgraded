package uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.amount

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import java.math.BigDecimal

internal data class SalePaymentAmount(
    val payment: ReceiptPayment,
    val leftAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val creditAdvanceHolder: CreditAdvanceHolder?
)