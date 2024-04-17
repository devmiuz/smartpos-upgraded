package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewPagerDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.helper.payment.discount.R
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.selection.di.DiscountSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.discount.databinding.FragmentFeatureHelperPaymentDiscountSelectionBinding as ViewBinding

internal class DiscountSelectionFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_payment_discount_selection),
    IHasComponent<DiscountSelectionComponent>, DiscountSelectionView {

    @Inject
    lateinit var lazyPresenter: Lazy<DiscountSelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): DiscountSelectionComponent =
        DiscountSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            viewPagerDelegate.onCreate(
                view = viewPager,
                adapter = FragmentStatePagerAdapter(fragment = this@DiscountSelectionFragment),
                savedInstanceState = savedInstanceState
            )

            tabLayout.setupWithViewPager(viewPager)
        }
    }

    companion object {

        fun newInstance() =
            DiscountSelectionFragment().withArguments()
    }
}