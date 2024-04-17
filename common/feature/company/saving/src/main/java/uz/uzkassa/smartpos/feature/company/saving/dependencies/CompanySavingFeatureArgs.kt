package uz.uzkassa.smartpos.feature.company.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

interface CompanySavingFeatureArgs {

    val activityTypesBroadcastChannel: BroadcastChannelWrapper<List<ActivityType>>

    val companyVATBroadcastChannel: BroadcastChannelWrapper<CompanyVAT>

    val regionCityBroadcastChannel: BroadcastChannelWrapper<Pair<Region, City>>
}