package uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies

import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import java.math.BigDecimal

interface PaymentAmountFeatureArgs {

    val amount: BigDecimal

    val allowedAmount: BigDecimal?

    val amountType: AmountType

    val branchId: Long

    val leftAmount: BigDecimal

    val totalAmount: BigDecimal

    val creditAdvanceHolder: CreditAdvanceHolder?
}