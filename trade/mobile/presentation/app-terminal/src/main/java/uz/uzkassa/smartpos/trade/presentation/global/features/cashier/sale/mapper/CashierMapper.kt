package uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.Discount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountArbitrary
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount as PaymentAmount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType as PaymentAmountType
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.Discount as PaymentDiscount
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary as PaymentDiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountType as PaymentDiscountType

internal fun PaymentAmount.map(): Amount =
    Amount(amount, changeAmount, leftAmount, totalAmount, type.map(), creditAdvanceHolder)

internal fun PaymentAmountType.map(): Type =
    when (this) {
        PaymentAmountType.CASH -> Type.CASH
        PaymentAmountType.OTHER -> Type.OTHER
        PaymentAmountType.HUMO->Type.HUMO
        else -> Type.CARD
    }

internal fun Type.map(): PaymentAmountType =
    when (this) {
        Type.CASH -> PaymentAmountType.CASH
        Type.OTHER -> PaymentAmountType.OTHER
        Type.HUMO -> PaymentAmountType.HUMO
        else -> PaymentAmountType.CARD
    }

internal fun PaymentDiscount.map(): Discount =
    Discount(discountArbitrary.map())

internal fun PaymentDiscountArbitrary?.map(): DiscountArbitrary? =
    this?.let {
        DiscountArbitrary(
            actualAmount = actualAmount,
            amount = amount,
            discountAmount = discountAmount,
            discountPercent = discountPercent,
            discountType = discountType.map()
        )
    }

internal fun PaymentDiscountType.map(): DiscountType = when (this) {
    PaymentDiscountType.BY_SUM -> DiscountType.BY_SUM
    PaymentDiscountType.BY_PERCENT -> DiscountType.BY_PERCENT
}
