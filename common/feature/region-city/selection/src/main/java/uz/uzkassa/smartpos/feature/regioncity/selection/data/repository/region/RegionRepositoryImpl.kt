package uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.region

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.region.service.RegionRestService
import javax.inject.Inject

internal class RegionRepositoryImpl @Inject constructor(
    private val regionEntityDao: RegionEntityDao,
    private val regionRestService: RegionRestService
) : RegionRepository {

    override fun getRegions(): Flow<List<Region>> {
        return regionRestService
            .getRegions()
            .onEach { regionEntityDao.upsert(it.mapToEntities()) }
            .map { it.map() }
    }
}