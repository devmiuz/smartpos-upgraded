package uz.uzkassa.smartpos.core.data.source.resource.company.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse

internal class CompanyRestServiceImpl(
    private val restServiceInternal: CompanyRestServiceInternal
) : CompanyRestService {

    override fun createCompany(jsonElement: JsonElement): Flow<CompanyResponse> {
        return restServiceInternal.createCompany(jsonElement)
    }

    override fun getCurrentCompany(): Flow<CompanyResponse> {
        return restServiceInternal.getCurrentCompany()
    }

    override fun getCompany(companyId: Long): Flow<CompanyResponse> {
        return restServiceInternal.getCompany(companyId)
    }

    override fun updateCompany(jsonElement: JsonElement): Flow<CompanyResponse> {
        return restServiceInternal.updateCompany(jsonElement)
    }
}