package uz.uzkassa.smartpos.feature.company.vat.selection.data.model

import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT

data class CompanyVATSelection internal constructor(
    val companyVAT: CompanyVAT,
    val isSelected: Boolean
)