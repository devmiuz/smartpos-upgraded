package uz.uzkassa.smartpos.core.data.source.resource.city.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse

internal class CityRestServiceImpl(
    private val restServiceInternal: CityRestServiceInternal
) : CityRestService {

    override fun getCities(regionId: Long): Flow<List<CityResponse>> {
        return restServiceInternal.getCities(regionId)
    }
}