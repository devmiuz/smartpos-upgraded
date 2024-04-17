package uz.uzkassa.smartpos.feature.branch.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

interface BranchSavingFeatureArgs {

    val activityTypeBroadcastChannel: BroadcastChannelWrapper<ActivityType>

    val branchId: Long?

    val regionCityBroadcastChannel: BroadcastChannelWrapper<Pair<Region, City>>
}