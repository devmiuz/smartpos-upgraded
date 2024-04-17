package uz.uzkassa.smartpos.feature.branch.saving.presentation.update

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region

internal interface BranchUpdateView : MvpView {

    fun onLoadingBranch()

    fun onSuccessBranch(branch: Branch)

    fun onErrorBranch(throwable: Throwable)

    fun onActivityTypeAdded(activityType: ActivityType)

    fun onActivityTypeRemoved(activityType: ActivityType)

    fun onRegionCityChanged(region: Region, city: City)

    fun onLoadingUpdate()

    fun onErrorUpdateCauseNameNotDefined()

    fun onErrorUpdateCauseRegionNotDefined()

    fun onErrorUpdateCauseCityNotDefined()

    fun onErrorUpdateCauseAddressNotDefined()

    fun onErrorUpdate(throwable: Throwable)
}