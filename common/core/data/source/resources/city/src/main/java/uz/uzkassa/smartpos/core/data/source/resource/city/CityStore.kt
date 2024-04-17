package uz.uzkassa.smartpos.core.data.source.resource.city

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.service.CityRestService

class CityStore(
    private val cityEntityDao: CityEntityDao,
    private val cityRestService: CityRestService
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getCitiesByRegionId(): Store<Long, List<City>> {
        return StoreBuilder.from<Long, List<CityResponse>, List<City>>(
            fetcher = Fetcher.ofFlow { cityRestService.getCities(it) },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { it -> cityEntityDao.getEntities(it).map { it.map() } },
                writer = { _, it -> cityEntityDao.upsert(it.mapToEntities()) },
                delete = { throw UnsupportedOperationException() },
                deleteAll = { cityEntityDao.deleteAll() }
            )
        ).disableCache().build()
    }
}