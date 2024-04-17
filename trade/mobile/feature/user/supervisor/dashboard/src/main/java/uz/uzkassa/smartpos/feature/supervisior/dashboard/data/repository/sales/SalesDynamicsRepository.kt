package uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.sales

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamics
import uz.uzkassa.smartpos.feature.analytics.sales.dynamics.data.model.granularity.Granularity
import java.util.*

internal interface SalesDynamicsRepository {

    fun getSalesDynamicsByFilter(
        fromDate: Date,
        toDate: Date,
        granularity: Granularity
    ): Flow<List<SalesDynamics>>

    fun getSalesDynamicsByDate(date: Date): Flow<SalesDynamics?>
}