package uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import java.math.BigDecimal

interface PaymentAmountFeatureRunner {

    fun run(
        creditAdvanceHolder: CreditAdvanceHolder?,
        amount: BigDecimal,
        allowedAmount: BigDecimal?,
        amountType: AmountType,
        branchId: Long,
        leftAmount: BigDecimal,
        totalAmount: BigDecimal,
        action: ((Screen) -> Unit)
    )

    fun finish(action: (Amount) -> Unit): PaymentAmountFeatureRunner
}