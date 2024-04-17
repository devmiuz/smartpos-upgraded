 package uz.uzkassa.smartpos.feature.supervisior.dashboard.data.mapper

import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsEntity
import uz.uzkassa.smartpos.core.utils.util.withoutTime
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.model.ReceiptTotalDetails
import java.math.BigDecimal

 internal fun ReceiptTotalDetails.mapToSalesDynamicsEntity(refund:BigDecimal): SalesDynamicsEntity =
    SalesDynamicsEntity(
        salesDate = date.withoutTime(),
        salesCount = count,
        salesCash = cashTotal,
        salesCard = cardTotal,
        discount = discount,
        refund = refund,
        vat = vat,
        salesTotal = total,
        averageReceiptCost = averageReceiptCost
    )