package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus

fun ReceiptDraftRelation.map() =
    ReceiptDraft(
        id = receiptDraftEntity.id,
        name = receiptDraftEntity.name,
        isRemote = receiptDraftEntity.isRemote,

        receipt = with(receiptEntity) {
            Receipt(
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
                status = ReceiptStatus.valueOf(status),
                receiptDetails = receiptDetailRelations.map(),
                receiptPayments = receiptPaymentEntities.map(),
                customerName = customerName,
                customerContact = customerContact,
                readonly = readonly,
                forceToPrint = forceToPrint,
                receiptSeq = receiptSeq,
                terminalId = terminalId,
                fiscalReceiptCreatedDate = fiscalReceiptCreatedDate,
                paymentBillId = paymentBillId,
                baseStatus = ReceiptStatus.valueOf(baseStatus),
                transactionId = transactionId,
                paymentProviderId = paymentProviderId
            )
        }
    )