package uz.uzkassa.smartpos.feature.regioncity.selection.domain

import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.feature.regioncity.selection.data.channel.RegionIdBroadcastChannel
import uz.uzkassa.smartpos.feature.regioncity.selection.data.channel.SelectionUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.regioncity.selection.data.model.RegionCitySelectionType
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureArgs
import javax.inject.Inject

internal class RegionCitySelectionInteractor @Inject constructor(
    private val regionCitySelectionFeatureArgs: RegionCitySelectionFeatureArgs,
    private val regionIdBroadcastChannel: RegionIdBroadcastChannel,
    private val selectionUnitBroadcastChannel: SelectionUnitBroadcastChannel
) {
    private var region: Region? = null
    private var city: City? = null

    val selectionType: RegionCitySelectionType =
        regionCitySelectionFeatureArgs.regionCitySelectionType

    fun getRegionCityPair(): Pair<Region, City> =
        Pair(checkNotNull(region), checkNotNull(city))

    fun setRegion(region: Region) {
        this.region = region
        city = null
        regionIdBroadcastChannel.sendBlocking(region.id)
    }

    fun setCity(city: City) {
        this.city = city
        selectionUnitBroadcastChannel.sendBlocking(Unit)
    }
}