package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.mapper

import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamics
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsEntity
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsResponse
import uz.uzkassa.smartpos.core.utils.util.withoutTime

fun SalesDynamicsResponse.mapToEntity(): SalesDynamicsEntity =
    SalesDynamicsEntity(
        salesDate = date.withoutTime(),
        salesCount = salesCount,
        salesCash = salesCash,
        salesCard = salesCard,
        discount = discount,
        vat = vat,
        refund = refund,
        salesTotal = salesTotal - discount, // TODO: 6/28/20 total = card + cash + vat + discount because needed minus discount
        averageReceiptCost = averageReceiptCost
    )

fun SalesDynamicsEntity.map(): SalesDynamics =
    SalesDynamics(
        salesDate = salesDate,
        salesCount = salesCount,
        salesCash = salesCash,
        salesCard = salesCard,
        discount = discount,
        vat = vat,
        refund = refund,
        salesTotal = salesTotal,
        salesIncome = salesTotal - vat - refund,
        averageReceiptCost = averageReceiptCost
    )