package uz.uzkassa.smartpos.feature.company.saving.data.repository.saving

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.company.saving.data.repository.saving.params.SaveCompanyParams

internal interface CompanySavingRepository {

    fun createCompany(params: SaveCompanyParams): Flow<Unit>

    fun updateCompany(params: SaveCompanyParams): Flow<Unit>
}