package uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales.wrapper

import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamics

data class SalesDynamicsWrapper internal constructor(
    val salesDynamics: SalesDynamics,
    val percentOfMaxSalesTotal: Float
)