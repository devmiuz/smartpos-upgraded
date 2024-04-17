package uz.uzkassa.smartpos.core.data.source.resource.card.mapper.type

import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass.CardClass
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardType
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeEntity

fun List<CardTypeEntity>.map() =
    map { it.map() }

fun CardTypeEntity.map() =
    CardType(
        id = id,
        createdById = createdById,
        updatedById = updatedById,
        discountPercent = discountPercent,
        cardClass = with(cardClass) { CardClass(type, nameRu) },
        name = name,
        description = description,
        createdDate = createdDate,
        updatedDate = updatedDate
    )