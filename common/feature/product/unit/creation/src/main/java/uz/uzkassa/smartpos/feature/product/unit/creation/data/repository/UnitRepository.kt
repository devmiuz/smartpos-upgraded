package uz.uzkassa.smartpos.feature.product.unit.creation.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit

internal interface UnitRepository {

    fun getUnits(): Flow<List<Unit>>
}