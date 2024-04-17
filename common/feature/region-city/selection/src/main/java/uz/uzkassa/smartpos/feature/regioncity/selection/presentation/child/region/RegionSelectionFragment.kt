package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.regioncity.selection.R
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region.di.RegionSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.regioncity.selection.databinding.FragmentFeatureRegionCityRegionSelectionBinding as ViewBinding

internal class RegionSelectionFragment
    : MvpAppCompatFragment(R.layout.fragment_feature_region_city_region_selection),
    IHasComponent<RegionSelectionComponent>, RegionSelectionView {

    @Inject
    lateinit var lazyPresenter: Lazy<RegionSelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val recyclerViewDelegate by lazy { RecyclerViewDelegate(this) { presenter.setRegion(it) } }

    override fun getComponent(): RegionSelectionComponent =
        RegionSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { recyclerViewDelegate.onCreate(recyclerView, savedInstanceState) }
    }

    override fun onLoadingRegion() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessRegion(regions: List<Region>) =
        recyclerViewDelegate.onSuccess(regions)

    override fun onErrorRegion(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getRegions() }

    companion object {

        fun newInstance() =
            RegionSelectionFragment().withArguments()
    }
}