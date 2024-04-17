package uz.uzkassa.smartpos.core.data.source.resource.unit.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitResponse

interface UnitRestService {

    fun getUnits(): Flow<List<UnitResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): UnitRestService =
            UnitRestServiceImpl(retrofit.create())
    }
}