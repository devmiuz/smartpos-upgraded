package uz.uzkassa.smartpos.trade.presentation.global.features.region_city.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.feature.regioncity.selection.data.model.RegionCitySelectionType
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface RegionCitySelectionFeatureRunner : FeatureRunner {

    fun run(
        regionId: Long?,
        cityId: Long?,
        regionCitySelectionType: RegionCitySelectionType,
        action: (Screen) -> Unit
    )

    fun finish(action: (Region, City) -> Unit): RegionCitySelectionFeatureRunner
}