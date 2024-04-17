package uz.uzkassa.smartpos.feature.company.vat.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT

interface CompanyVATSelectionFeatureCallback {

    fun onFinishCompanyVATSelection(companyVAT: CompanyVAT)
}