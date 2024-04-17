package uz.uzkassa.smartpos.feature.regioncity.selection.domain.city

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.regioncity.selection.data.repository.city.CityRepository
import javax.inject.Inject

internal class CityInteractor @Inject constructor(
    private val citySelectionRepository: CityRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getCities(regionId: Long): Flow<Result<List<City>>> {
        return citySelectionRepository
            .getCities(regionId)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}