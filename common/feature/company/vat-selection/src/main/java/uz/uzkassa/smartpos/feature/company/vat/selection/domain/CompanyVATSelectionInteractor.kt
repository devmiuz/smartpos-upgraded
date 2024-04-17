package uz.uzkassa.smartpos.feature.company.vat.selection.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.company.vat.selection.data.model.CompanyVATSelection
import uz.uzkassa.smartpos.feature.company.vat.selection.data.repository.CompanyVATRepository
import javax.inject.Inject

internal class CompanyVATSelectionInteractor @Inject constructor(
    private val companyVATRepository: CompanyVATRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var selectedCompanyVAT: CompanyVAT? = null

    fun getCompanyVAT(): Flow<Result<List<CompanyVATSelection>>> {
        return companyVATRepository
            .getCompanyVAT()
            .onEach { it -> selectedCompanyVAT = it.find { it.isSelected }?.companyVAT }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getSelectedCompanyVAT(): CompanyVAT? =
        selectedCompanyVAT

    fun setCompanyVAT(companyVAT: CompanyVAT) {
        selectedCompanyVAT = companyVAT
    }
}