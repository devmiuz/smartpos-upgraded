package uz.uzkassa.smartpos.feature.company.saving.data.repository.business

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service.CompanyBusinessTypeRestService
import javax.inject.Inject

internal class CompanyBusinessTypeRepositoryImpl @Inject constructor(
    private val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao,
    private val companyBusinessTypeRestService: CompanyBusinessTypeRestService
) : CompanyBusinessTypeRepository {

    override fun getCompanyBusinessTypes(): Flow<List<CompanyBusinessType>> {
        return companyBusinessTypeRestService
            .getCompanyBusinessTypes()
            .onEach { companyBusinessTypeEntityDao.upsert(it.mapToEntities()) }
            .map { it.map() }
    }
}