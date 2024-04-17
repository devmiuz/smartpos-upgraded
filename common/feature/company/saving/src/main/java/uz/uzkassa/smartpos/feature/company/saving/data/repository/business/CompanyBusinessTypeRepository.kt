package uz.uzkassa.smartpos.feature.company.saving.data.repository.business

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType

internal interface CompanyBusinessTypeRepository {

    fun getCompanyBusinessTypes(): Flow<List<CompanyBusinessType>>
}