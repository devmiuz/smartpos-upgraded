package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model

import androidx.room.Embedded
import androidx.room.Relation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmountEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetailEntity

data class PostponeReceiptRelation(
    @Embedded
    val postponeReceiptEntity: PostponeReceiptEntity,

    @Relation(
        parentColumn = "postpone_receipt_id",
        entityColumn = "postpone_receipt_amount_postpone_receipt_id"
    )
    val postponeReceiptAmountEntities: List<PostponeReceiptAmountEntity>,

    @Relation(
        parentColumn = "postpone_receipt_id",
        entityColumn = "postpone_receipt_detail_postpone_receipt_id"
    )
    val postponeReceiptDetailEntities: List<PostponeReceiptDetailEntity> = emptyList()
)