package uz.uzkassa.smartpos.feature.branch.saving.presentation.creation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region

internal interface BranchCreationView : MvpView {

    fun onActivityTypeAdded(activityType: ActivityType)

    fun onActivityTypeRemoved(activityType: ActivityType)

    fun onRegionCityChanged(region: Region, city: City)

    fun onLoadingCreation()

    fun onErrorCreationCauseNameNotDefined()

    fun onErrorCreationCauseRegionNotDefined()

    fun onErrorCreationCauseCityNotDefined()

    fun onErrorCreationCauseAddressNotDefined()

    fun onErrorCreation(throwable: Throwable)
}