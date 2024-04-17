package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse

internal class CompanyBusinessTypeRestServiceImpl(
    private val businessTypeRestServiceInternal: CompanyBusinessTypeRestServiceInternal
) : CompanyBusinessTypeRestService {

    override fun getCompanyBusinessTypes(): Flow<List<CompanyBusinessTypeResponse>> {
        return businessTypeRestServiceInternal.getCompanyBusinessTypes()
    }
}