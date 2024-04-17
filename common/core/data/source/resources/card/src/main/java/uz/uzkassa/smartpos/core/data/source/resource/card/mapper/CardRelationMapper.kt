package uz.uzkassa.smartpos.core.data.source.resource.card.mapper

import uz.uzkassa.smartpos.core.data.source.resource.card.mapper.type.map
import uz.uzkassa.smartpos.core.data.source.resource.card.model.Card
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardRelation
import uz.uzkassa.smartpos.core.data.source.resource.customer.mapper.map

fun List<CardRelation>.map() =
    map { it.map() }

fun CardRelation.map() =
    Card(
        id = cardEntity.id,
        activatedByUserId = cardEntity.activatedByUserId,
        createdById = cardEntity.createdById,
        updatedById = cardEntity.updatedById,
        issuedByBranchId = cardEntity.issuedByBranchId,
        isActivated = cardEntity.isActivated,
        isBlocked = cardEntity.isBlocked,
        balance = cardEntity.balance,
        totalDiscount = cardEntity.totalDiscount,
        cardNumber = cardEntity.cardNumber,
        cardType = cardTypeEntity.map(),
        customer = customerEntity?.map(),
        activatedDate = cardEntity.activatedDate,
        createdDate = cardEntity.createdDate,
        updatedDate = cardEntity.updatedDate
    )