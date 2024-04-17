package uz.uzkassa.smartpos.core.data.source.resource.unit

import uz.uzkassa.smartpos.core.data.source.resource.store.Store
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitResponse
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService

class UnitStore(
    private val unitEntityDao: UnitEntityDao,
    private val unitRestService: UnitRestService
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getUnits(): Store<kotlin.Unit, List<Unit>> {
        return Store.from<kotlin.Unit, List<UnitResponse>, List<Unit>>(
            fetcher = Fetcher.ofFlow { unitRestService.getUnits() },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { unitEntityDao.getEntities().map { it.map() } },
                writer = { _, it -> unitEntityDao.upsert(it.mapToEntities()) }
            )
        )
    }
}