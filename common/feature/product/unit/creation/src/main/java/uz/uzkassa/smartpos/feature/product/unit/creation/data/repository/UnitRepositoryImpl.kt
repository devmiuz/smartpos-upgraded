package uz.uzkassa.smartpos.feature.product.unit.creation.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import javax.inject.Inject

internal class UnitRepositoryImpl @Inject constructor(
    private val unitEntityDao: UnitEntityDao,
    private val unitRestService: UnitRestService
) : UnitRepository {

    @FlowPreview
    override fun getUnits(): Flow<List<Unit>> {
        return unitRestService.getUnits()
            .onEach { entities -> unitEntityDao.upsert(entities.mapToEntities()) }
            .flatMapConcat { flow { emit(unitEntityDao.getEntities()) } }
            .map { it.map() }
    }
}