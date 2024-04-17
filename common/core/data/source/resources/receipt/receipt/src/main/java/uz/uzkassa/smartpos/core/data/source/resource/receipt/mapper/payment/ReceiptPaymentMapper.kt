package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentEntity

fun List<ReceiptPayment>.mapToEntity(receiptUid: String) =
    map { it.mapToEntity(receiptUid) }

fun ReceiptPayment.mapToEntity(receiptUid: String) =
    ReceiptPaymentEntity(0, receiptUid, amount, type.name)