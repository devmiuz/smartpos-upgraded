package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import java.math.BigDecimal

fun List<ReceiptResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun List<ReceiptResponse>.mapToEntitiesWithBaseStatus(baseStatus: ReceiptStatus) =
    map {
        it.mapToEntity(baseStatus)
    }

fun ReceiptResponse.mapToEntity() =
    ReceiptEntity(
        uid = uid,
        originUid = originUid,
        userId = userId,
        customerId = customerId,
        loyaltyCardId = loyaltyCardId,
        shiftId = shiftId,
        branchId = branchId,
        companyId = companyId,
        receiptDate = receiptDate,
        receiptLatitude = receiptLatitude,
        receiptLongitude = receiptLongitude,
        shiftNumber = shiftNumber,
        discountPercent = discountPercent ?: 0.0,
        totalCard = totalCard ?: BigDecimal.ZERO,
        totalCash = totalCash ?: BigDecimal.ZERO,
        totalCost = totalCost,
        totalDiscount = totalDiscount ?: BigDecimal.ZERO,
        totalExcise = totalExcise ?: BigDecimal.ZERO,
        totalLoyaltyCard = totalLoyaltyCard ?: BigDecimal.ZERO,
        totalVAT = totalVAT,
        totalPaid = totalPaid,
        terminalModel = terminalModel,
        terminalSerialNumber = terminalSerialNumber,
        fiscalSign = fiscalSign,
        fiscalUrl = fiscalUrl,
        status = status.name,
        isSynced = true,
        customerName = customerName,
        customerContact = customerContact,
        readonly = readonly,
        forceToPrint = forceToPrint,
        receiptSeq = additionalDetails?.receiptSeq,
        terminalId = additionalDetails?.terminalId,
        fiscalReceiptCreatedDate = additionalDetails?.fiscalReceiptCreatedDate,
        paymentBillId = paymentBillId,
        baseStatus = status.name,
        transactionId = additionalDetails?.transactionId,
        paymentProviderId = additionalDetails?.providerId
    )

fun ReceiptResponse.mapToEntity(baseStatus: ReceiptStatus) =
    ReceiptEntity(
        uid = uid,
        originUid = originUid,
        userId = userId,
        customerId = customerId,
        loyaltyCardId = loyaltyCardId,
        shiftId = shiftId,
        branchId = branchId,
        companyId = companyId,
        receiptDate = receiptDate,
        receiptLatitude = receiptLatitude,
        receiptLongitude = receiptLongitude,
        shiftNumber = shiftNumber,
        discountPercent = discountPercent ?: 0.0,
        totalCard = totalCard ?: BigDecimal.ZERO,
        totalCash = totalCash ?: BigDecimal.ZERO,
        totalCost = totalCost,
        totalDiscount = totalDiscount ?: BigDecimal.ZERO,
        totalExcise = totalExcise ?: BigDecimal.ZERO,
        totalLoyaltyCard = totalLoyaltyCard ?: BigDecimal.ZERO,
        totalVAT = totalVAT,
        totalPaid = totalPaid,
        terminalModel = terminalModel,
        terminalSerialNumber = terminalSerialNumber,
        fiscalSign = fiscalSign,
        fiscalUrl = fiscalUrl,
        status = status.name,
        isSynced = true,
        customerName = customerName,
        customerContact = customerContact,
        readonly = readonly,
        forceToPrint = forceToPrint,
        receiptSeq = additionalDetails?.receiptSeq,
        terminalId = additionalDetails?.terminalId,
        fiscalReceiptCreatedDate = additionalDetails?.fiscalReceiptCreatedDate,
        paymentBillId = paymentBillId,
        baseStatus = baseStatus.name,
        transactionId = additionalDetails?.transactionId,
        paymentProviderId = additionalDetails?.providerId
    )