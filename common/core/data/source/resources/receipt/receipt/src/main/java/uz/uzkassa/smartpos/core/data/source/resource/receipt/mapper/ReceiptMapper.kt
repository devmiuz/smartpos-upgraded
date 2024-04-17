package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptEntity

fun Receipt.mapToEntity(isSynced: Boolean) =
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
        status = status.name,
        isSynced = isSynced,
        readonly = readonly,
        forceToPrint = forceToPrint,
        customerName = customerName,
        customerContact = customerContact,
        receiptSeq = receiptSeq,
        terminalId = terminalId,
        fiscalReceiptCreatedDate = fiscalReceiptCreatedDate,
        paymentBillId = paymentBillId,
        baseStatus = baseStatus.name,
        transactionId = transactionId,
        paymentProviderId = paymentProviderId
    )