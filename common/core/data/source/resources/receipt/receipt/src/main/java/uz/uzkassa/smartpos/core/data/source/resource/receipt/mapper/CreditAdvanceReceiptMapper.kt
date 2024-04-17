package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.AdditionalDetails
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.CreditAdvanceReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse

fun List<CreditAdvanceReceiptResponse>.mapToReceiptResonses() =
    map { it.mapToReceiptResponse() }

fun CreditAdvanceReceiptResponse.mapToReceiptResponse() =
    ReceiptResponse(
        uid = uid,
        originUid = originUid,
        userResponse = ReceiptResponse.ReceiptUserResponse(userId),
        customerId = customerId,
        loyaltyCardId = loyaltyCardId,
        shiftId = shiftId,
        branchId = branchId,
        companyId = companyId,
        receiptDate = receiptDate,
        receiptLatitude = receiptLatitude,
        receiptLongitude = receiptLongitude,
        shiftNumber = shiftNumber,
        discountPercent = discountPercent,
        totalCard = totalCard,
        totalCash = totalCash,
        totalCost = totalCost,
        totalDiscount = totalDiscount,
        totalExcise = totalExcise,
        totalLoyaltyCard = totalLoyaltyCard,
        totalVAT = totalVAT,
        totalPaid = totalPaid,
        terminalModel = terminalModel,
        terminalSerialNumber = terminalSerialNumber,
        fiscalSign = fiscalSign,
        fiscalUrl = fiscalUrl,
        status = status,
        receiptDetails = receiptDetails,
        receiptPayments = receiptPayments,
        readonly = readonly,
        forceToPrint = forceToPrint,
        customerName = customerName,
        customerContact = customerContact,
        additionalDetails = additionalDetails,
        paymentBillId = paymentBillId
    )