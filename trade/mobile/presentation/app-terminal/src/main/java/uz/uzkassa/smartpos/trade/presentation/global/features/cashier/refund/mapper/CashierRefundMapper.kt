package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.mapper

import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantityResult
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount as PaymentAmount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType as PaymentAmountType
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantityResult as ProductProductQuantityResult

internal fun AmountType.map(): PaymentAmountType =
    when (this) {
        AmountType.CARD -> PaymentAmountType.CARD
        AmountType.CASH -> PaymentAmountType.CASH
    }

internal fun PaymentAmountType.map(): AmountType =
    when (this) {
        PaymentAmountType.CARD -> AmountType.CARD
        else -> AmountType.CASH
    }

internal fun PaymentAmount.map(): Amount =
    Amount(amount, changeAmount, leftAmount, totalAmount, type.map())