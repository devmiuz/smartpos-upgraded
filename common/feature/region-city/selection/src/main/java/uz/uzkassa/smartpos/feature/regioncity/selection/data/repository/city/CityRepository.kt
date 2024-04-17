package uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.city

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City

internal interface CityRepository {

    fun getCities(regionId: Long): Flow<List<City>>
}