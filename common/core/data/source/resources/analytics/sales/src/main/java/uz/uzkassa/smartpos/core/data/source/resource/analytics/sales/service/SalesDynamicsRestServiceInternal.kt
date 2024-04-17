package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsResponse

internal interface SalesDynamicsRestServiceInternal {

    @GET(API_SALES_DYNAMICS)
    fun getSalesDynamics(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Query(QUERY_FROM) fromDate: String,
        @Query(QUERY_TO) toDate: String,
        @Query(QUERY_GRANULARITY) granularity: String
    ): Flow<List<SalesDynamicsResponse>>

    private companion object {
        const val API_SALES_DYNAMICS: String = "api/analytics/sales-dynamics"
        const val QUERY_BRANCH_ID: String = "branchId"
        const val QUERY_FROM: String = "from"
        const val QUERY_GRANULARITY: String = "granularity"
        const val QUERY_TO: String = "to"
    }
}