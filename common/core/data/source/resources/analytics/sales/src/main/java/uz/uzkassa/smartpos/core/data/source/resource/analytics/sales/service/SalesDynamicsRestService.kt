package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsResponse

interface SalesDynamicsRestService {

    fun getSalesDynamics(
        branchId: Long,
        fromDate: String,
        toDate: String,
        granularity: String
    ): Flow<List<SalesDynamicsResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): SalesDynamicsRestService =
            SalesDynamicsRestServiceImpl(retrofit.create())
    }
}