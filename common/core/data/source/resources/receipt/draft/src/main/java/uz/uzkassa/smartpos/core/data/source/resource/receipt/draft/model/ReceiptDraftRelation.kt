package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Relation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentEntity

@Entity(tableName = "receipt_drafts")
data class ReceiptDraftRelation @Ignore internal constructor(
    @Embedded
    val receiptDraftEntity: ReceiptDraftEntity,

    @Embedded
    val receiptEntity: ReceiptEntity,

    @Relation(parentColumn = "receipt_uid", entityColumn = "receipt_payment_receipt_uid")
    val receiptPaymentEntities: List<ReceiptPaymentEntity>,

    @Ignore
    val receiptDetailRelations: List<ReceiptDetailRelation>
) {

    internal constructor(
        receiptDraftEntity: ReceiptDraftEntity,
        receiptEntity: ReceiptEntity,
        receiptPaymentEntities: List<ReceiptPaymentEntity>
    ) : this(
        receiptDraftEntity = receiptDraftEntity,
        receiptEntity = receiptEntity,
        receiptPaymentEntities = receiptPaymentEntities,
        receiptDetailRelations = emptyList()
    )
}