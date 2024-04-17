package uz.uzkassa.smartpos.core.data.source.resource.card.mapper

import uz.uzkassa.smartpos.core.data.source.resource.card.mapper.type.map
import uz.uzkassa.smartpos.core.data.source.resource.card.model.Card
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardResponse
import uz.uzkassa.smartpos.core.data.source.resource.customer.mapper.map

fun List<CardResponse>.map() =
    map { it.map() }

fun List<CardResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun CardResponse.map() =
    Card(
        id = id,
        activatedByUserId = activatedByUserId,
        createdById = createdById,
        updatedById = updatedById,
        issuedByBranchId = issuedByBranchId,
        isActivated = isActivated,
        isBlocked = isBlocked,
        balance = balance,
        totalDiscount = totalDiscount,
        cardNumber = cardNumber,
        cardType = cardType.map(),
        customer = customer?.map(),
        activatedDate = activatedDate,
        createdDate = createdDate,
        updatedDate = updatedDate
    )

fun CardResponse.mapToEntity() =
    CardEntity(
        id = id,
        cardTypeId = cardType.id,
        customerId = customer?.id,
        activatedByUserId = activatedByUserId,
        createdById = createdById,
        updatedById = updatedById,
        issuedByBranchId = issuedByBranchId,
        isActivated = isActivated,
        isBlocked = isBlocked,
        balance = balance,
        totalDiscount = totalDiscount,
        cardNumber = cardNumber,
        activatedDate = activatedDate,
        createdDate = createdDate,
        updatedDate = updatedDate
    )