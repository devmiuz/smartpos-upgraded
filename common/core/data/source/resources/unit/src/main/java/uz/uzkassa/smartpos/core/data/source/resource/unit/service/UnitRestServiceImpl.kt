package uz.uzkassa.smartpos.core.data.source.resource.unit.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitResponse

internal class UnitRestServiceImpl(
    private val restServiceInternal: UnitRestServiceInternal
) : UnitRestService {

    override fun getUnits(): Flow<List<UnitResponse>> {
        return restServiceInternal.getUnits()
    }
}