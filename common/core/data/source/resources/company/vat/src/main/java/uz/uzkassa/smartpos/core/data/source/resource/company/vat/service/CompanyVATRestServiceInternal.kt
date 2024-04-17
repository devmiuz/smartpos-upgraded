package uz.uzkassa.smartpos.core.data.source.resource.company.vat.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVATResponse

internal interface CompanyVATRestServiceInternal {

    @GET(API_COMPANY_VAT)
    fun getCompanyVAT(): Flow<List<CompanyVATResponse>>

    private companion object {
        const val API_COMPANY_VAT: String = "api/company/vat"
    }
}