package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City

internal interface CitySelectionView : MvpView {

    fun onLoadingCities()

    fun onSuccessCities(cities: List<City>)

    fun onErrorCities(throwable: Throwable)
}