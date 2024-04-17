package uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT

interface CompanyVATSelectionFeatureRunner {

    fun run(companyVAT: CompanyVAT?, action: (Screen) -> Unit)

    fun run(vatPercent: Double?, action: (Screen) -> Unit)

    fun finish(action: (CompanyVAT) -> Unit): CompanyVATSelectionFeatureRunner
}