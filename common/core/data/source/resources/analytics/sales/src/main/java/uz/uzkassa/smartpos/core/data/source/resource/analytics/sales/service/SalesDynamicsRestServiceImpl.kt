package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsResponse

internal class SalesDynamicsRestServiceImpl(
    private val salesDynamicsRestServiceInternal: SalesDynamicsRestServiceInternal
) : SalesDynamicsRestService {

    override fun getSalesDynamics(
        branchId: Long,
        fromDate: String,
        toDate: String,
        granularity: String
    ): Flow<List<SalesDynamicsResponse>> {
        return salesDynamicsRestServiceInternal.getSalesDynamics(
            branchId = branchId,
            fromDate = fromDate,
            toDate = toDate,
            granularity = granularity
        )
    }
}