package uz.uzkassa.smartpos.trade.presentation.global.features.company.vat

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureArgs
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureCallback
import uz.uzkassa.smartpos.feature.company.vat.selection.presentation.CompanyVATSelectionFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.runner.CompanyVATSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class CompanyVATSelectionFeatureMediator : FeatureMediator,
    CompanyVATSelectionFeatureArgs, CompanyVATSelectionFeatureCallback {
    private var finishAction: ((CompanyVAT) -> Unit) by Delegates.notNull()
    override var companyVAT: CompanyVAT? = null
    override var vatPercent: Double? = null

    val featureRunner: CompanyVATSelectionFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishCompanyVATSelection(companyVAT: CompanyVAT) =
        finishAction.invoke(companyVAT)

    private inner class FeatureRunnerImpl : CompanyVATSelectionFeatureRunner {

        override fun run(companyVAT: CompanyVAT?, action: (Screen) -> Unit) {
            this@CompanyVATSelectionFeatureMediator.companyVAT = companyVAT
            action.invoke(Screens.CompanyVATSelectionScreen)
        }

        override fun run(vatPercent: Double?, action: (Screen) -> Unit) {
            this@CompanyVATSelectionFeatureMediator.vatPercent = vatPercent
            action.invoke(Screens.CompanyVATSelectionScreen)
        }

        override fun finish(action: (CompanyVAT) -> Unit): CompanyVATSelectionFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object CompanyVATSelectionScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CompanyVATSelectionFragment.newInstance()
        }
    }
}