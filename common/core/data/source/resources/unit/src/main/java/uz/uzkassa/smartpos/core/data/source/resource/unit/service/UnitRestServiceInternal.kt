package uz.uzkassa.smartpos.core.data.source.resource.unit.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitResponse

internal interface UnitRestServiceInternal {

    @GET(API_PRODUCT_UNIT)
    fun getUnits(): Flow<List<UnitResponse>>

    private companion object {
        const val API_PRODUCT_UNIT: String = "api/product/units"
    }
}