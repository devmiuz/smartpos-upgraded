package uz.uzkassa.smartpos.feature.helper.payment.amount.data.model

import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import java.math.BigDecimal

data class Amount(
    val amount: BigDecimal,
    val changeAmount: BigDecimal,
    val leftAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val type: AmountType,
    val creditAdvanceHolder: CreditAdvanceHolder?
)