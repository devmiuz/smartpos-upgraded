package uz.uzkassa.smartpos.trade.presentation.global.features.company.saving

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureArgs
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureCallback
import uz.uzkassa.smartpos.feature.company.saving.presentation.creation.CompanyCreationFragment
import uz.uzkassa.smartpos.feature.regioncity.selection.data.model.RegionCitySelectionType
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.runner.ActivityTypeSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.runner.CompanySavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.runner.CompanyVATSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.runner.RegionCitySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import javax.inject.Inject
import kotlin.properties.Delegates
import uz.uzkassa.smartpos.feature.company.saving.data.model.RegionCitySelectionType as CompanyRegionCitySelectionType

class CompanySavingFeatureMediator @Inject constructor(
    private val activityTypeSelectionFeatureRunner: ActivityTypeSelectionFeatureRunner,
    private val companyVATSelectionFeatureRunner: CompanyVATSelectionFeatureRunner,
    private val regionCitySelectionFeatureRunner: RegionCitySelectionFeatureRunner,
    private val router: Router
) : FeatureMediator, CompanySavingFeatureArgs, CompanySavingFeatureCallback {
    override val activityTypesBroadcastChannel = BroadcastChannelWrapper<List<ActivityType>>()
    override val companyVATBroadcastChannel = BroadcastChannelWrapper<CompanyVAT>()
    override val regionCityBroadcastChannel = BroadcastChannelWrapper<Pair<Region, City>>()
    private var finishAction: (() -> Unit) by Delegates.notNull()

    val featureRunner: CompanySavingFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenActivityTypeSelection(activityTypes: List<ActivityType>) {
        activityTypeSelectionFeatureRunner
            .finish { activityTypesBroadcastChannel.sendBlocking(it) }
            .run(activityTypes, isMultiSelection = true) { router.navigateTo(it) }
    }

    override fun onOpenRegionCitySelection(
        region: Region?,
        city: City?,
        regionCitySelectionType: CompanyRegionCitySelectionType
    ) {
        regionCitySelectionFeatureRunner
            .finish { t1, t2 -> regionCityBroadcastChannel.sendBlocking(t1 to t2) }
            .run(
                region?.id,
                city?.id,
                RegionCitySelectionType.valueOf(regionCitySelectionType.name)
            ) { router.navigateTo(it) }
    }

    override fun onOpenCompanyVATSelection(companyVAT: CompanyVAT?) {
        companyVATSelectionFeatureRunner
            .finish { companyVATBroadcastChannel.sendBlocking(it) }
            .run(companyVAT) { router.navigateTo(it) }
    }

    override fun onFinishCompanySaving() =
        finishAction.invoke()

    private inner class FeatureRunnerImpl : CompanySavingFeatureRunner {

        override fun run(action: (Screen) -> Unit) {
            action.invoke(Screens.CompanyCreationScreen)
        }

        override fun finish(action: () -> Unit): CompanySavingFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object CompanyCreationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CompanyCreationFragment.newInstance()
        }
    }
}