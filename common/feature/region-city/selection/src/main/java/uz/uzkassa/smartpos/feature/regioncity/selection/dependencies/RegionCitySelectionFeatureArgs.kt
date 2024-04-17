package uz.uzkassa.smartpos.feature.regioncity.selection.dependencies

import uz.uzkassa.smartpos.feature.regioncity.selection.data.model.RegionCitySelectionType

interface RegionCitySelectionFeatureArgs {

    val regionId: Long?

    val cityId: Long?

    val regionCitySelectionType: RegionCitySelectionType
}