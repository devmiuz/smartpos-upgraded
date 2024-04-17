package uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass

import uz.uzkassa.smartpos.core.utils.enums.EnumCompanion
import uz.uzkassa.smartpos.core.utils.enums.valueOrDefault

data class CardClass constructor(
    val type: Type,
    val nameRu: String
) {

    constructor(type: String, nameRu: String) : this(Type.valueOrDefault(type), nameRu)

    enum class Type {
        ACCUMULATIVE,
        DISCOUNT,
        UNKNOWN;

        companion object : EnumCompanion<Type> {
            override val DEFAULT: Type = UNKNOWN
        }
    }

}