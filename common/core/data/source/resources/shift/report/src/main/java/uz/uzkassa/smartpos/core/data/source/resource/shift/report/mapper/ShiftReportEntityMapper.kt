package uz.uzkassa.smartpos.core.data.source.resource.shift.report.mapper

import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReport
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportEntity
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportResponse

fun ShiftReportEntity.map() =
    ShiftReport(
        startDate = startDate,
        finishDate = finishDate,
        fiscalShiftNumber = fiscalShiftNumber,
        totalRefundVAT = totalRefundVAT,
        totalRefundCard = totalRefundCard,
        totalRefundCash = totalRefundCash,
        totalRefundCount = totalRefundCount,
        totalSaleVAT = totalSaleVAT,
        totalSaleCard = totalSaleCard,
        totalSaleCash = totalSaleCash,
        totalSaleCount = totalSaleCount
    )

fun ShiftReportEntity.mapToResponse() =
    ShiftReportResponse(
        startDate = startDate,
        finishDate = finishDate,
        fiscalShiftNumber = fiscalShiftNumber,
        totalRefundVAT = totalRefundVAT,
        totalRefundCard = totalRefundCard,
        totalRefundCash = totalRefundCash,
        totalRefundCount = totalRefundCount,
        totalSaleVAT = totalSaleVAT,
        totalSaleCard = totalSaleCard,
        totalSaleCash = totalSaleCash,
        totalSaleCount = totalSaleCount,
        userId = userId
    )