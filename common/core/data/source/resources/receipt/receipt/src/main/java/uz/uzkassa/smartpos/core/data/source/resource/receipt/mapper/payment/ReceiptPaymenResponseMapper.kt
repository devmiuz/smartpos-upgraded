package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentListResponse.ReceiptPaymentResponse

fun List<ReceiptPaymentResponse>.mapToEntities(receiptUid: String) =
    map { it.mapToEntity(receiptUid) }

fun ReceiptPaymentResponse.mapToEntity(receiptUid: String) =
    ReceiptPaymentEntity(0 ,receiptUid, amount, type.name)