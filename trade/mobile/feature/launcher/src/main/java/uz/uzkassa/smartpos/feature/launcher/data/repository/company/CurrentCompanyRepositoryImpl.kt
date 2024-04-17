package uz.uzkassa.smartpos.feature.launcher.data.repository.company

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import javax.inject.Inject

internal class CurrentCompanyRepositoryImpl @Inject constructor(
    private val companyRelationDao: CompanyRelationDao
) : CurrentCompanyRepository {

    @FlowPreview
    override fun getCurrentCompany(): Flow<Company> {
        return companyRelationDao.getRelationFlow()
            .filterNotNull()
            .map { it.map() }
    }
}