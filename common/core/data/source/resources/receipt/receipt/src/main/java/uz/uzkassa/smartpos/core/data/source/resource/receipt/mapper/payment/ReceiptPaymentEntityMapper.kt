package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentListResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentListResponse.ReceiptPaymentResponse
import uz.uzkassa.smartpos.core.utils.enums.valueOrDefault

fun List<ReceiptPaymentEntity>.map() =
    map { it.map() }

fun List<ReceiptPaymentEntity>.mapToResponses() =
    ReceiptPaymentListResponse(map { it.mapToResponse() })

fun ReceiptPaymentEntity.map() =
    ReceiptPayment(amount, ReceiptPayment.Type.valueOrDefault(type))

fun ReceiptPaymentEntity.mapToResponse() =
    ReceiptPaymentResponse(amount, ReceiptPaymentResponse.Type.valueOrDefault(type))