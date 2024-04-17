package uz.uzkassa.smartpos.core.data.source.resource.region.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse

internal interface RegionRestServiceInternal {

    @GET(API_REGIONS)
    fun getRegions(): Flow<List<RegionResponse>>

    private companion object {
        const val API_REGIONS: String = "api/regions"
    }
}