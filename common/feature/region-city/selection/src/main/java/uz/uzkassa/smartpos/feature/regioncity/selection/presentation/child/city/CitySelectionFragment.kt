package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.regioncity.selection.R
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.di.CitySelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.regioncity.selection.databinding.FragmentFeatureRegionCityCitySelectionBinding as ViewBinding

internal class CitySelectionFragment
    : MvpAppCompatFragment(R.layout.fragment_feature_region_city_city_selection),
    IHasComponent<CitySelectionComponent>, CitySelectionView {

    @Inject
    lateinit var lazyPresenter: Lazy<CitySelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val recyclerViewDelegate by lazy { RecyclerViewDelegate(this) { presenter.setCity(it) } }

    override fun getComponent(): CitySelectionComponent =
        CitySelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { recyclerViewDelegate.onCreate(recyclerView, savedInstanceState) }
    }

    override fun onLoadingCities() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessCities(cities: List<City>) =
        recyclerViewDelegate.onSuccess(cities)

    override fun onErrorCities(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getCities() }

    companion object {

        fun newInstance() =
            CitySelectionFragment().withArguments()
    }
}