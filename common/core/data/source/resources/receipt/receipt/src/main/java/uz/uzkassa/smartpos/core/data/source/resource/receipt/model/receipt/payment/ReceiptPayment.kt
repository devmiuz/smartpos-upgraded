package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment

import uz.uzkassa.smartpos.core.utils.enums.EnumCompanion
import java.math.BigDecimal

data class ReceiptPayment(
    val amount: BigDecimal,
    val type: Type
) {

    @Suppress("SpellCheckingInspection")
    enum class Type {
        CARD,
        CASH,
        APAY,
        DISCOUNT,
        EXCISE,
        HUMO,
        LOYALTY_CARD,
        NDS,
        OTHER,
        UZCARD;

        companion object : EnumCompanion<Type> {
            override val DEFAULT: Type = OTHER
        }
    }
}