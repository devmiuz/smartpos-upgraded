package uz.uzkassa.smartpos.core.data.source.resource.region.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse

internal class RegionRestServiceImpl(
    private val restServiceInternal: RegionRestServiceInternal
) : RegionRestService {

    override fun getRegions(): Flow<List<RegionResponse>> {
        return restServiceInternal.getRegions()
    }
}