package uz.uzkassa.smartpos.feature.company.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.feature.company.saving.data.model.RegionCitySelectionType

interface CompanySavingFeatureCallback {

    fun onOpenActivityTypeSelection(activityTypes: List<ActivityType>)

    fun onOpenRegionCitySelection(region: Region?, city: City?, regionCitySelectionType: RegionCitySelectionType)

    fun onOpenCompanyVATSelection(companyVAT: CompanyVAT?)

    fun onFinishCompanySaving()
}