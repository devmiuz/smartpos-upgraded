package uz.uzkassa.smartpos.core.data.source.resource.company.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse

interface CompanyRestService {

    fun createCompany(jsonElement: JsonElement): Flow<CompanyResponse>

    fun getCurrentCompany(): Flow<CompanyResponse>

    fun getCompany(companyId: Long): Flow<CompanyResponse>

    fun updateCompany(jsonElement: JsonElement): Flow<CompanyResponse>

    companion object {

        fun instantiate(retrofit: Retrofit): CompanyRestService =
            CompanyRestServiceImpl(retrofit.create())
    }
}