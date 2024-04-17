package uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.region

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region

internal interface RegionRepository {

    fun getRegions(): Flow<List<Region>>
}