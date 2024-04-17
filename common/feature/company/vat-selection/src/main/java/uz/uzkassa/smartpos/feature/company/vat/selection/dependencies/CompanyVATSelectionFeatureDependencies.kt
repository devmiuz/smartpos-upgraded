package uz.uzkassa.smartpos.feature.company.vat.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.company.vat.service.CompanyVATRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CompanyVATSelectionFeatureDependencies {

    val companyVATRestService: CompanyVATRestService

    val companyVATSelectionFeatureArgs: CompanyVATSelectionFeatureArgs

    val companyVATSelectionFeatureCallback: CompanyVATSelectionFeatureCallback

    val coroutineContextManager: CoroutineContextManager
}