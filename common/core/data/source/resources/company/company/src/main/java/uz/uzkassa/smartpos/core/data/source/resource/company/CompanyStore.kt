package uz.uzkassa.smartpos.core.data.source.resource.company

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyRelation
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.data.source.resource.store.Store
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth

class CompanyStore(
    private val companyEntityDao: CompanyEntityDao,
    private val companyRelationDao: CompanyRelationDao,
    private val companyRestService: CompanyRestService
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getCurrentCompany(): Store<Unit, Company> {
        return Store.from<Unit, CompanyResponse, Company>(
            fetcher = Fetcher.ofFlow {
                companyRestService.getCurrentCompany()
                    .catch {
                        if (it is NotFoundHttpException) companyEntityDao.deleteAll()
                        throw it
                    }
            },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { companyRelationDao.getRelation().map() },
                writer = { _, it -> companyEntityDao.save(it) }
            )
        )
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getCurrentCompanyForPayment(): Flow<CompanyRelation?> {
        return companyRelationDao.getRelationFlow()
    }
}