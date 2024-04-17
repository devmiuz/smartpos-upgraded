package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.AdditionalDetails
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import java.util.*

fun List<ReceiptRelation>.map() =
    map { it.map() }

fun List<ReceiptRelation>.mapToResponses() =
    map { it.mapToResponse() }

fun ReceiptRelation.map(): Receipt {
    val currentDate: Date = Date().let {
        return@let if (receiptEntity.receiptDate.time > it.time) it
        else receiptEntity.receiptDate
    }

    return Receipt(
        uid = receiptEntity.uid,
        originUid = receiptEntity.originUid,
        userId = receiptEntity.userId,
        customerId = receiptEntity.customerId,
        loyaltyCardId = receiptEntity.loyaltyCardId,
        shiftId = receiptEntity.shiftId,
        branchId = receiptEntity.branchId,
        companyId = receiptEntity.companyId,
        receiptDate = currentDate,
        receiptLatitude = receiptEntity.receiptLatitude,
        receiptLongitude = receiptEntity.receiptLongitude,
        shiftNumber = receiptEntity.shiftNumber,
        discountPercent = receiptEntity.discountPercent,
        totalCard = receiptEntity.totalCard,
        totalCash = receiptEntity.totalCash,
        totalCost = receiptEntity.totalCost,
        totalDiscount = receiptEntity.totalDiscount,
        totalExcise = receiptEntity.totalExcise,
        totalLoyaltyCard = receiptEntity.totalLoyaltyCard,
        totalVAT = receiptEntity.totalVAT,
        totalPaid = receiptEntity.totalPaid,
        terminalModel = receiptEntity.terminalModel,
        terminalSerialNumber = receiptEntity.terminalSerialNumber,
        fiscalSign = receiptEntity.fiscalSign,
        fiscalUrl = receiptEntity.fiscalUrl,
        status = ReceiptStatus.valueOf(receiptEntity.status),
        receiptDetails = receiptDetailRelations.map(),
        receiptPayments = receiptPaymentEntities.map(),
        customerName = receiptEntity.customerName,
        customerContact = receiptEntity.customerContact,
        readonly = receiptEntity.readonly,
        forceToPrint = receiptEntity.forceToPrint,
        terminalId = receiptEntity.terminalId,
        receiptSeq = receiptEntity.receiptSeq,
        fiscalReceiptCreatedDate = receiptEntity.fiscalReceiptCreatedDate,
        paymentBillId = receiptEntity.paymentBillId,
        baseStatus = ReceiptStatus.valueOf(receiptEntity.baseStatus),
        transactionId = receiptEntity.transactionId,
        paymentProviderId = receiptEntity.paymentProviderId
    )

}

fun ReceiptRelation.mapToResponse(): ReceiptResponse {
    val currentDate: Date = Date().let {
        return@let if (receiptEntity.receiptDate.time > it.time) it
        else receiptEntity.receiptDate
    }

    return ReceiptResponse(
        uid = receiptEntity.uid,
        originUid = receiptEntity.originUid,
        userResponse = ReceiptResponse.ReceiptUserResponse(receiptEntity.userId),
        customerId = receiptEntity.customerId,
        loyaltyCardId = receiptEntity.loyaltyCardId,
        shiftId = receiptEntity.shiftId,
        branchId = receiptEntity.branchId,
        companyId = receiptEntity.companyId,
        receiptDate = currentDate,
        receiptLatitude = receiptEntity.receiptLatitude,
        receiptLongitude = receiptEntity.receiptLongitude,
        shiftNumber = receiptEntity.shiftNumber,
        discountPercent = receiptEntity.discountPercent,
        totalCard = receiptEntity.totalCard,
        totalCash = receiptEntity.totalCash,
        totalCost = receiptEntity.totalCost,
        totalDiscount = receiptEntity.totalDiscount,
        totalExcise = receiptEntity.totalExcise,
        totalLoyaltyCard = receiptEntity.totalLoyaltyCard,
        totalVAT = receiptEntity.totalVAT,
        totalPaid = receiptEntity.totalPaid,
        terminalModel = receiptEntity.terminalModel,
        terminalSerialNumber = receiptEntity.terminalSerialNumber,
        fiscalSign = receiptEntity.fiscalSign,
        fiscalUrl = receiptEntity.fiscalUrl,
        status = ReceiptStatusResponse.valueOf(receiptEntity.status),
        receiptDetails = receiptDetailRelations.mapToResponse(),
        receiptPayments = receiptPaymentEntities.mapToResponses(),
        readonly = receiptEntity.readonly,
        forceToPrint = receiptEntity.forceToPrint,
        customerName = receiptEntity.customerName,
        customerContact = receiptEntity.customerContact,
        additionalDetails = AdditionalDetails(
            receiptEntity.receiptSeq,
            receiptEntity.terminalId,
            receiptEntity.fiscalReceiptCreatedDate,
            receiptEntity.paymentProviderId,
            receiptEntity.transactionId
        ),
        paymentBillId = receiptEntity.paymentBillId
    )
}