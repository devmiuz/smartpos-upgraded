package uz.uzkassa.smartpos.core.data.source.resource.card.mapper.type

import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass.CardClass
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass.CardClassEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardType
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeResponse

fun List<CardTypeResponse>.map() =
    map { it.map() }

fun List<CardTypeResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun CardTypeResponse.map() =
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

fun CardTypeResponse.mapToEntity() =
    CardTypeEntity(
        id = id,
        createdById = createdById,
        updatedById = updatedById,
        discountPercent = discountPercent,
        cardClass = with(cardClass) { CardClassEntity(type, nameRu) },
        name = name,
        description = description,
        createdDate = createdDate,
        updatedDate = updatedDate
    )