package uz.uzkassa.smartpos.core.data.source.resource.company.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.*
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse

internal interface CompanyRestServiceInternal {

    @POST(API_COMPANY)
    fun createCompany(@Body jsonElement: JsonElement): Flow<CompanyResponse>

    @GET(API_COMPANY_CURRENT)
    fun getCurrentCompany(): Flow<CompanyResponse>

    @GET("$API_COMPANY/{id}")
    fun getCompany(@Path("id") id: Long): Flow<CompanyResponse>

    @PUT(API_COMPANY)
    fun updateCompany(@Body jsonElement: JsonElement): Flow<CompanyResponse>

    private companion object {
        const val API_COMPANY: String = "api/company"
        const val API_COMPANY_CURRENT: String = "api/company/current"
    }
}