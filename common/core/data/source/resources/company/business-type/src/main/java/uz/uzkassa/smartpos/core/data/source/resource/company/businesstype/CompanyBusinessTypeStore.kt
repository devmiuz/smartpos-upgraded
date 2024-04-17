package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service.CompanyBusinessTypeRestService

class CompanyBusinessTypeStore(
    private val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao,
    private val companyBusinessTypeRestService: CompanyBusinessTypeRestService
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun getCompanyBusinessTypes(): Store<Unit, List<CompanyBusinessType>> {
        return StoreBuilder.from<Unit, List<CompanyBusinessTypeResponse>, List<CompanyBusinessType>>(
            fetcher = Fetcher.ofFlow { companyBusinessTypeRestService.getCompanyBusinessTypes() },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { companyBusinessTypeEntityDao.getEntities().map { it.map() } },
                writer = { _, it -> companyBusinessTypeEntityDao.upsert(it.mapToEntities()) },
                delete = { throw UnsupportedOperationException() },
                deleteAll = { companyBusinessTypeEntityDao.deleteAll() }
            )
        ).disableCache().build()
    }
}