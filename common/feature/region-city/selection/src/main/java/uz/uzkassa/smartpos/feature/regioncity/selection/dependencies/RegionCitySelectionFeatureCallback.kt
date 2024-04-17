package uz.uzkassa.smartpos.feature.regioncity.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region

interface RegionCitySelectionFeatureCallback {

    fun onFinishRegionCitySelection(region: Region, city: City)
}