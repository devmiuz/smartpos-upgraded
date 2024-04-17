package uz.uzkassa.smartpos.core.data.source.resource.region.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse

interface RegionRestService {

    fun getRegions(): Flow<List<RegionResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): RegionRestService =
            RegionRestServiceImpl(retrofit.create())
    }
}