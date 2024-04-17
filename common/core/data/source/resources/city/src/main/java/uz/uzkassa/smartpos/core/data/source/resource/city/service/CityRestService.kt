package uz.uzkassa.smartpos.core.data.source.resource.city.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse

interface CityRestService {

    fun getCities(regionId: Long): Flow<List<CityResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): CityRestService =
            CityRestServiceImpl(retrofit.create())
    }
}