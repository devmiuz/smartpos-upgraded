package uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.city

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.city.service.CityRestService
import javax.inject.Inject

internal class CityRepositoryImpl @Inject constructor(
    private val cityEntityDao: CityEntityDao,
    private val cityRestService: CityRestService
) : CityRepository {

    override fun getCities(regionId: Long): Flow<List<City>> {
        return cityRestService
            .getCities(regionId)
            .onEach { cityEntityDao.upsert(it.mapToEntities()) }
            .map { it.map() }
    }
}