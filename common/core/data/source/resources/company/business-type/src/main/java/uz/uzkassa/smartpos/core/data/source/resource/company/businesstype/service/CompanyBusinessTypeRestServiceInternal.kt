package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse

internal interface CompanyBusinessTypeRestServiceInternal {

    @GET(API_COMPANY_BUSINESS_TYPES)
    fun getCompanyBusinessTypes(): Flow<List<CompanyBusinessTypeResponse>>

    private companion object {
        const val API_COMPANY_BUSINESS_TYPES: String = "api/company/business-types"
    }
}