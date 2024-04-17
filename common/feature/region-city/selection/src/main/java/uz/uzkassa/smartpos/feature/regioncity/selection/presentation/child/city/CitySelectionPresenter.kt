package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.RegionCitySelectionInteractor
import uz.uzkassa.smartpos.feature.regioncity.selection.domain.city.CityInteractor
import javax.inject.Inject
import kotlin.properties.Delegates

internal class CitySelectionPresenter @Inject constructor(
    private val cityInteractor: CityInteractor,
    private val regionCitySelectionInteractor: RegionCitySelectionInteractor,
    private val regionIdLazyFlow: Lazy<Flow<Long>>
) : MvpPresenter<CitySelectionView>() {
    private var regionId: Long by Delegates.notNull()

    override fun onFirstViewAttach() =
        getRegionId()

    fun getCities() {
        cityInteractor.getCities(regionId)
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCities() }
            .onSuccess { viewState.onSuccessCities(it) }
            .onFailure { viewState.onErrorCities(it) }
    }

    fun setCity(city: City) =
        regionCitySelectionInteractor.setCity(city)

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun getRegionId() {
        regionIdLazyFlow.get()
            .onEach { regionId = it }
            .flatMapMerge { cityInteractor.getCities(regionId) }
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCities() }
            .onSuccess { viewState.onSuccessCities(it) }
            .onFailure { viewState.onErrorCities(it) }
    }
}