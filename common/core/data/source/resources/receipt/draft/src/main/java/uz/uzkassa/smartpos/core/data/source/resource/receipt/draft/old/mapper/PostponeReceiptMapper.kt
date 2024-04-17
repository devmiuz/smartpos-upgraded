package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.PostponeReceipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.PostponeReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.PostponeReceiptRelation

fun List<PostponeReceiptRelation>.map() =
    map { it.map() }

fun PostponeReceiptRelation.map() =
    PostponeReceipt(
        id = postponeReceiptEntity.id,
        uid = postponeReceiptEntity.uid,
        receiptDate = postponeReceiptEntity.receiptDate,
        cardNumber = postponeReceiptEntity.cardNumber,
        name = postponeReceiptEntity.name,
        amount = postponeReceiptEntity.amount,
        discountAmount = postponeReceiptEntity.discountAmount,
        discountPercent = postponeReceiptEntity.discountPercent,
        receiptAmounts = postponeReceiptAmountEntities.map(),
        receiptDetails = postponeReceiptDetailEntities.map()
    )

fun PostponeReceipt.mapToEntity() =
    PostponeReceiptEntity(
        id = id,
        uid = uid,
        receiptDate = receiptDate,
        name = name,
        amount = amount,
        cardNumber = cardNumber,
        discountAmount = discountAmount,
        discountPercent = discountPercent
    )