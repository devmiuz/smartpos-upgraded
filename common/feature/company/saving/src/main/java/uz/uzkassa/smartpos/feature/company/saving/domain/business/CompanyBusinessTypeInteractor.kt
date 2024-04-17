package uz.uzkassa.smartpos.feature.company.saving.domain.business

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.company.saving.data.repository.business.CompanyBusinessTypeRepository
import javax.inject.Inject

internal class CompanyBusinessTypeInteractor @Inject constructor(
    private val companyBusinessTypeRepository: CompanyBusinessTypeRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getCompanyBusinessTypes(): Flow<Result<List<CompanyBusinessType>>> {
        return companyBusinessTypeRepository
            .getCompanyBusinessTypes()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}