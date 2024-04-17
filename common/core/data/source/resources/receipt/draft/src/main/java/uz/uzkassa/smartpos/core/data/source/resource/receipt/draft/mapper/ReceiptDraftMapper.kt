package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftEntity

fun ReceiptDraft.mapToEntity() =
    ReceiptDraftEntity(
        id = id,
        receiptUid = receipt.uid,
        name = name,
        isRemote = isRemote
    )