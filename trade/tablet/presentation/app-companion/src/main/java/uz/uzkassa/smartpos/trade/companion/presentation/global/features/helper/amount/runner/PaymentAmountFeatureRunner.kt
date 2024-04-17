package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.amount.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType
import java.math.BigDecimal

interface PaymentAmountFeatureRunner {

    fun run(
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