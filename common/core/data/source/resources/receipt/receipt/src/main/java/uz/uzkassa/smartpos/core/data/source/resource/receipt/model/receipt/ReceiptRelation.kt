package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentEntity

data class ReceiptRelation @Ignore internal constructor(
    @Embedded
    val receiptEntity: ReceiptEntity,

    @Relation(parentColumn = "receipt_uid", entityColumn = "receipt_payment_receipt_uid")
    val receiptPaymentEntities: List<ReceiptPaymentEntity>,

    @Ignore
    val receiptDetailRelations: List<ReceiptDetailRelation>
) {

    internal constructor(
        receiptEntity: ReceiptEntity,
        receiptPaymentEntities: List<ReceiptPaymentEntity>
    ) : this(
        receiptEntity = receiptEntity,
        receiptPaymentEntities = receiptPaymentEntities,
        receiptDetailRelations = emptyList()
    )
}