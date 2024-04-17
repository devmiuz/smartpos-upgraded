package uz.uzkassa.smartpos.core.data.utils.vat

import java.math.BigDecimal
import java.math.RoundingMode

class VATAllocator(amount: BigDecimal, quantity: Double, val vatRate: Double?) {

    constructor(
        amount: BigDecimal,
        quantity: Double,
        vatRate: BigDecimal?
    ) : this(
        amount = amount,
        quantity = quantity,
        vatRate = vatRate?.toDouble()
    )

    val vatAmount: BigDecimal by lazy {
        return@lazy if (vatRate != null) (
                amount
                    .multiply(BigDecimal(quantity))
                    .multiply(BigDecimal(vatRate))
                    .divide((vatRate + 100.00).toBigDecimal(), 7, RoundingMode.HALF_UP)
                )
        else BigDecimal.ZERO
    }
}