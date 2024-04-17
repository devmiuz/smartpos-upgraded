package uz.uzkassa.smartpos.core.data.source.fiscal.mapper.shift

import uz.uzkassa.cto.fiscal.integration.model.shift.finish.FiscalFinishShiftResponse
import uz.uzkassa.cto.fiscal.integration.model.shift.start.FiscalStartShiftResponse
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalCloseShiftResult
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalOpenShiftResult

internal fun FiscalStartShiftResponse.mapToFiscalResult(): FiscalOpenShiftResult =
    FiscalOpenShiftResult(openDate, samSerialNumber, shiftNumber)

internal fun FiscalFinishShiftResponse.mapToFiscalResult(): FiscalCloseShiftResult =
    FiscalCloseShiftResult(
        startDate = startDate,
        finishDate = finishDate,
        samSerialNumber = samSerialNumber,
        shiftNumber = shiftNumber,
        firstReceiptSeq = firstReceiptSeq,
        lastReceiptSeq = lastReceiptSeq,
        totalRefundVAT = totalRefundVAT,
        totalRefundCard = totalRefundCard,
        totalRefundCash = totalRefundCash,
        totalRefundCount = totalRefundCount,
        totalSaleVAT = totalSaleVAT,
        totalSaleCard = totalSaleCard,
        totalSaleCash = totalSaleCash,
        totalSaleCount = totalSaleCount
    )