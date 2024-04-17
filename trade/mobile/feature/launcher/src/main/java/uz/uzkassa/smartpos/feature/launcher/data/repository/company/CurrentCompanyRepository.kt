package uz.uzkassa.smartpos.feature.launcher.data.repository.company

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company

internal interface CurrentCompanyRepository {

    fun getCurrentCompany(): Flow<Company>
}