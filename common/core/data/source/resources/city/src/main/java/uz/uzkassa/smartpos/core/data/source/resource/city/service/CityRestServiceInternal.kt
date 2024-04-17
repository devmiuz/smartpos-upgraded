package uz.uzkassa.smartpos.core.data.source.resource.city.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse

internal interface CityRestServiceInternal {

    @GET("$API_CITIES/{$PATH_ID}")
    fun getCities(@Path(PATH_ID) regionId: Long): Flow<List<CityResponse>>

    private companion object {
        const val API_CITIES: String = "api/cities"
        const val PATH_ID: String = "id"
    }
}