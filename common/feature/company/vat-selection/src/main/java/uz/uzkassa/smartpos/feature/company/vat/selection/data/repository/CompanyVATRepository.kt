package uz.uzkassa.smartpos.feature.company.vat.selection.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.company.vat.selection.data.model.CompanyVATSelection

internal interface CompanyVATRepository {

    fun getCompanyVAT(): Flow<List<CompanyVATSelection>>
}