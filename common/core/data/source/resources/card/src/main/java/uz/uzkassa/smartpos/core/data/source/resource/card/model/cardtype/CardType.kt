package uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype

import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass.CardClass
import java.util.*

data class CardType(
    val id: Long,
    val createdById: Long,
    val updatedById: Long?,
    val discountPercent: Double,
    val cardClass: CardClass,
    val name: String,
    val description: String?,
    val createdDate: Date,
    val updatedDate: Date?
)