package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmount
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmountEntity

fun List<PostponeReceiptAmount>.mapToEntities(receiptId: Long) =
    map { it.mapToEntity(receiptId) }

fun List<PostponeReceiptAmountEntity>.map() =
    map { it.map() }

fun PostponeReceiptAmount.mapToEntity(receiptId: Long) =
    uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmountEntity(
        receiptId = receiptId,
        amount = amount,
        type = type.name
    )

fun PostponeReceiptAmountEntity.map() =
    PostponeReceiptAmount(
        amount = amount,
        type = PostponeReceiptAmount.Type.valueOf(type)
    )