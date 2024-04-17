package uz.uzkassa.smartpos.core.data.source.resource.region

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.service.RegionRestService

class RegionStore(
    private val regionEntityDao: RegionEntityDao,
    private val regionRestService: RegionRestService
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getRegions(): Store<Unit, List<Region>> {
        return StoreBuilder.from<Unit, List<RegionResponse>, List<Region>>(
            fetcher = Fetcher.ofFlow { regionRestService.getRegions() },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { regionEntityDao.getEntities().map { it.map() } },
                writer = { _, it -> regionEntityDao.insertOrUpdate(it.mapToEntities()) },
                delete = { throw UnsupportedOperationException() },
                deleteAll = { regionEntityDao.deleteAll() }
            )
        ).disableCache().build()
    }
}