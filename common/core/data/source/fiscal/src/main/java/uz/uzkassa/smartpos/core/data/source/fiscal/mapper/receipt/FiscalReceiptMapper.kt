package uz.uzkassa.smartpos.core.data.source.fiscal.mapper.receipt

import uz.uzkassa.cto.fiscal.integration.model.receipt.ExtraInfo
import uz.uzkassa.cto.fiscal.integration.model.receipt.FiscalReceiptRequest
import uz.uzkassa.cto.fiscal.integration.model.receipt.FiscalReceiptRequest.FiscalReceiptType
import uz.uzkassa.cto.fiscal.integration.model.receipt.FiscalReceiptResponse
import uz.uzkassa.cto.fiscal.integration.model.receipt.RefundInfo
import uz.uzkassa.cto.fiscal.integration.model.receipt.details.FiscalReceiptDetailsRequest
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.*
import uz.uzkassa.smartpos.core.utils.math.times
import uz.uzkassa.smartpos.core.utils.primitives.roundToBigDecimal
import java.math.BigDecimal

internal fun FiscalReceipt.mapToFiscalRequest(): FiscalReceiptRequest {
    return FiscalReceiptRequest(
        receivedCard = totalCard * 100,
        receivedCash = totalCash * 100,
        latitude = latitude,
        longitude = longitude,
        creationDate = receiptDate,
        receiptType = when (receiptType) {
            FiscalReceipt.ReceiptType.REFUND -> FiscalReceiptType.REFUND
            FiscalReceipt.ReceiptType.SALE -> FiscalReceiptType.SALE
            FiscalReceipt.ReceiptType.CREDIT -> FiscalReceiptType.CREDIT
            FiscalReceipt.ReceiptType.ADVANCE -> FiscalReceiptType.ADVANCE
        },
        details = receiptDetails.map { it.mapToFiscalRequest() },
        refundInfo = receiptRefundInfo?.mapToFiscalReceiptRefundInfo(),
        receiptUid = receiptUid,
        extraInfo = extraInfo?.mapToFiscalReceiptExtraInfo()
    )
}

internal fun ReceiptRefundInfo.mapToFiscalReceiptRefundInfo(): RefundInfo {
    return RefundInfo(
        terminalID = terminalID,
        receiptSeq = receiptSeq,
        fiscalReceiptCreatedDate = fiscalReceiptCreatedDate,
        fiscalSign = fiscalSign
    )
}

internal fun FiscalExtraInfo.mapToFiscalReceiptExtraInfo(): ExtraInfo {
    return ExtraInfo(
        carNumber = carNumber,
        other = other,
        phoneNumber = phoneNumber,
        pinfl = pinfl,
        qrPaymentId = qrPaymentId,
        qrPaymentProvider = qrPaymentProvider,
        tin = tin
    )
}

internal fun FiscalReceiptResponse.mapToFiscalResult() =
    FiscalReceiptResult(
        receiptSeq = receiptSeq,
        shiftNumber = shiftNumber,
        createdDate = createdDate,

        receiptNumberInCurrentShift = receiptNumberInCurrentShift,
        samSerialNumber = samSerialNumber,
        fiscalSign = fiscalSign,
        fiscalUrl = fiscalUrl
    )

private fun FiscalReceiptDetail.mapToFiscalRequest() =
    FiscalReceiptDetailsRequest(
        amount = quantity * 1000,
        barcode = barcode,
        vat = vatAmount * 100,
        discount = discountAmount * 100,
        other = BigDecimal.ZERO,
        price = ((price * quantity) * 100).roundToBigDecimal(),
        label = label,
        name = name,
        unitName = unitName,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        vatPercent = vatPercent,
        unitId = unitId
    )