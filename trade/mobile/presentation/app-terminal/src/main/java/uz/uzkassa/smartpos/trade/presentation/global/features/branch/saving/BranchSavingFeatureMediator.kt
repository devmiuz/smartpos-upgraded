package uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureArgs
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureCallback
import uz.uzkassa.smartpos.feature.branch.saving.presentation.creation.BranchCreationFragment
import uz.uzkassa.smartpos.feature.branch.saving.presentation.update.BranchUpdateFragment
import uz.uzkassa.smartpos.feature.regioncity.selection.data.model.RegionCitySelectionType
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.runner.ActivityTypeSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.runner.RegionCitySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates
import uz.uzkassa.smartpos.feature.branch.saving.data.model.RegionCitySelectionType as BranchRegionCitySelectionType

class BranchSavingFeatureMediator(
    private val activityTypeSelectionFeatureRunner: ActivityTypeSelectionFeatureRunner,
    private val regionCitySelectionFeatureRunner: RegionCitySelectionFeatureRunner,
    private val router: Router
) : FeatureMediator, BranchSavingFeatureArgs, BranchSavingFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: ((branchId: Long) -> Unit)? = null
    override val activityTypeBroadcastChannel = BroadcastChannelWrapper<ActivityType>()
    override var branchId: Long? = null
    override val regionCityBroadcastChannel = BroadcastChannelWrapper<Pair<Region, City>>()

    val featureRunner: BranchSavingFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenActivityTypeSelection(activityType: ActivityType?) {
        activityTypeSelectionFeatureRunner
            .finish { activityTypeBroadcastChannel.sendBlocking(it.first()) }
            .run(listOfNotNull(activityType), isMultiSelection = false) { router.navigateTo(it) }
    }

    override fun onOpenRegionCitySelection(
        region: Region?,
        city: City?,
        regionCitySelectionType: BranchRegionCitySelectionType
    ) {
        regionCitySelectionFeatureRunner
            .finish { t1, t2 -> regionCityBroadcastChannel.sendBlocking(t1 to t2) }
            .run(
                region?.id,
                city?.id,
                RegionCitySelectionType.valueOf(regionCitySelectionType.name)
            ) { router.navigateTo(it) }
    }

    override fun onBackFromBranchSaving() =
        backAction.invoke()

    override fun onFinishBranchSaving(branchId: Long) {
        finishAction?.invoke(branchId)
    }

    private inner class FeatureRunnerImpl : BranchSavingFeatureRunner {

        override fun run(branchId: Long?, action: (Screen) -> Unit) {
            this@BranchSavingFeatureMediator.branchId = branchId
            val screen: Screen =
                if (branchId == null) Screens.BranchCreateScreen
                else Screens.BranchUpdateScreen
            action.invoke(screen)
        }

        override fun back(action: () -> Unit): BranchSavingFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: (branchId: Long) -> Unit): BranchSavingFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object BranchCreateScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                BranchCreationFragment.newInstance()
        }

        object BranchUpdateScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                BranchUpdateFragment.newInstance()
        }
    }
}