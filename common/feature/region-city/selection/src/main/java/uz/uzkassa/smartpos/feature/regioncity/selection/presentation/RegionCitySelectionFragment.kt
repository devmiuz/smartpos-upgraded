package uz.uzkassa.smartpos.feature.regioncity.selection.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewPagerDelegate
import uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager.ViewPagerNavigator
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.regioncity.selection.R
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.regioncity.selection.presentation.di.RegionCitySelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.regioncity.selection.databinding.FragmentFeatureRegionCitySelectionBinding as ViewBinding

class RegionCitySelectionFragment : MvpAppCompatDialogFragment(),
    IHasComponent<RegionCitySelectionComponent>, RegionCitySelectionView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<RegionCitySelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var viewPagerNavigator: ViewPagerNavigator

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private lateinit var binding: ViewBinding
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): RegionCitySelectionComponent =
        RegionCitySelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) {
            when {
                viewPagerDelegate.isFirstPosition -> presenter.dismiss().let { true }
                else -> presenter.openRegionSelection().let { true }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

            viewPagerDelegate.onCreate(
                view = viewPager,
                adapter = FragmentStatePagerAdapter(this@RegionCitySelectionFragment),
                isPagingEnabled = false,
                savedInstanceState = savedInstanceState
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewPagerNavigator.attachViewPager(binding.viewPager)
    }

    override fun onPause() {
        viewPagerNavigator.detach()
        super.onPause()
    }

    override fun onVisibleRegionSelectionScreen() =
        toolbarDelegate.setTitle(R.string.fragment_feature_region_city_region_selection_title)

    override fun onVisibleCitySelectionScreen() =
        toolbarDelegate.setTitle(R.string.fragment_feature_region_city_city_selection_title)

    override fun onDismissView() =
        dismissAllowingStateLoss()

    companion object {

        fun newInstance() =
            RegionCitySelectionFragment().withArguments()
    }
}