package uz.uzkassa.smartpos.core.data.source.resource.company.vat.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVATResponse

internal class CompanyVATRestServiceImpl(
    private val companyVATRestServiceInternal: CompanyVATRestServiceInternal
) : CompanyVATRestService {

    override fun getCompanyVAT(): Flow<List<CompanyVATResponse>> {
        return companyVATRestServiceInternal.getCompanyVAT()
    }
}