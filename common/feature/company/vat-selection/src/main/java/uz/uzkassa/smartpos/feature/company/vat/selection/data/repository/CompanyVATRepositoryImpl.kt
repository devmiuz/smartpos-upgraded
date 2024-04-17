package uz.uzkassa.smartpos.feature.company.vat.selection.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.service.CompanyVATRestService
import uz.uzkassa.smartpos.feature.company.vat.selection.data.model.CompanyVATSelection
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureArgs
import javax.inject.Inject

internal class CompanyVATRepositoryImpl @Inject constructor(
    private val companyVATRestService: CompanyVATRestService,
    private val companyVATSelectionFeatureArgs: CompanyVATSelectionFeatureArgs
) : CompanyVATRepository {
    private val companyVAT: CompanyVAT? = companyVATSelectionFeatureArgs.companyVAT
    private val vatPercent: Double? = companyVATSelectionFeatureArgs.vatPercent

    @Suppress("LABEL_NAME_CLASH")
    override fun getCompanyVAT(): Flow<List<CompanyVATSelection>> {
        return companyVATRestService.getCompanyVAT()
            .map { it ->
                it.map {
                    val vatPercent: Double? = companyVAT?.percent ?: vatPercent
                    val companyVAT: CompanyVAT = it.map()
                    return@map CompanyVATSelection(companyVAT, companyVAT.percent == vatPercent)
                }
            }
    }
}