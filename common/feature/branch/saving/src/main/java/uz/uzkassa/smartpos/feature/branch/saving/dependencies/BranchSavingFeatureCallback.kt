package uz.uzkassa.smartpos.feature.branch.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.feature.branch.saving.data.model.RegionCitySelectionType

interface BranchSavingFeatureCallback {

    fun onOpenActivityTypeSelection(activityType: ActivityType?)

    fun onOpenRegionCitySelection(region: Region?, city: City?, regionCitySelectionType: RegionCitySelectionType)

    fun onBackFromBranchSaving()

    fun onFinishBranchSaving(branchId: Long)
}