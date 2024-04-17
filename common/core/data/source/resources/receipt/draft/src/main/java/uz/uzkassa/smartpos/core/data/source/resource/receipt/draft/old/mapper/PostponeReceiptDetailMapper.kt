package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetailEntity

fun List<PostponeReceiptDetailEntity>.map() =
    map { it.map() }

fun List<PostponeReceiptDetail>.mapToEntities(receiptId: Long) =
    map { it.mapToEntity(receiptId) }

fun PostponeReceiptDetailEntity.map() =
    uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetail(
        productId = productId,
        unitId = unitId,
        amount = amount,
        quantity = quantity
    )

fun PostponeReceiptDetail.mapToEntity(receiptId: Long) =
    PostponeReceiptDetailEntity(
        receiptId = receiptId,
        productId = productId,
        unitId = unitId,
        amount = amount,
        quantity = quantity
    )