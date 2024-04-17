package uz.uzkassa.smartpos.feature.company.saving.data.repository.saving

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.feature.company.saving.data.repository.saving.params.SaveCompanyParams
import javax.inject.Inject

internal class CompanySavingRepositoryImpl @Inject constructor(
    private val companyEntityDao: CompanyEntityDao,
    private val companyRestService: CompanyRestService
) : CompanySavingRepository {

    override fun createCompany(params: SaveCompanyParams): Flow<Unit> {
        return companyRestService.createCompany(params.asJsonElement())
            .onEach { companyEntityDao.upsert(it.mapToEntity()) }
            .map { Unit }
    }

    override fun updateCompany(params: SaveCompanyParams): Flow<Unit> {
        return companyRestService.updateCompany(params.asJsonElement())
            .onEach { companyEntityDao.upsert(it.mapToEntity()) }
            .map { Unit }
    }
}