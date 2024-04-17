package uz.uzkassa.smartpos.trade.presentation.global.features.region_city

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.feature.regioncity.selection.data.model.RegionCitySelectionType
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureArgs
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureCallback
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.RegionCitySelectionFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.runner.RegionCitySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class RegionCitySelectionFeatureMediator : FeatureMediator,
    RegionCitySelectionFeatureArgs, RegionCitySelectionFeatureCallback {
    override var regionId: Long? = null
    override var cityId: Long? = null
    override var regionCitySelectionType: RegionCitySelectionType by Delegates.notNull()

    private var finishAction: ((Region, City) -> Unit) by Delegates.notNull()

    val featureRunner: RegionCitySelectionFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishRegionCitySelection(region: Region, city: City) =
        finishAction.invoke(region, city)

    private inner class FeatureRunnerImpl : RegionCitySelectionFeatureRunner {
        override fun run(
            regionId: Long?,
            cityId: Long?,
            regionCitySelectionType: RegionCitySelectionType,
            action: (Screen) -> Unit
        ) {
            this@RegionCitySelectionFeatureMediator.regionId = regionId
            this@RegionCitySelectionFeatureMediator.cityId = cityId
            this@RegionCitySelectionFeatureMediator.regionCitySelectionType = regionCitySelectionType
            action.invoke(Screens.RegionCitySelectionScreen)
        }

        override fun finish(action: (Region, City) -> Unit): RegionCitySelectionFeatureRunner {
            finishAction = action
            return this
        }

    }

    private object Screens {

        object RegionCitySelectionScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                RegionCitySelectionFragment.newInstance()
        }
    }
}