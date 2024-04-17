package uz.uzkassa.smartpos.feature.regioncity.selection.domain.region

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.region.RegionRepository
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureArgs
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.RegionCitySelectionInteractor
import javax.inject.Inject

internal class RegionInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val regionCitySelectionFeatureArgs: RegionCitySelectionFeatureArgs,
    private val regionCitySelectionInteractor: RegionCitySelectionInteractor,
    private val regionSelectionRepository: RegionRepository
) {

    fun getRegions(): Flow<Result<List<Region>>> {
        return regionSelectionRepository
            .getRegions()
            .onEach { regions ->
                regionCitySelectionFeatureArgs.regionId?.let { regionId ->
                    regions.find { it.id == regionId }?.let {
                        regionCitySelectionInteractor.setRegion(it)
                    }
                }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}