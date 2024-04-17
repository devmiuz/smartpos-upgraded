package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary

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
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.helper.payment.discount.R
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.adapter.FragmentStatePagerAdapter
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.features.arbitrary.di.ArbitraryComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.helper.payment.discount.databinding.FragmentFeatureHelperPaymentDiscountArbitraryBinding as ViewBinding

internal class ArbitraryFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_helper_payment_discount_arbitrary),
    IHasComponent<ArbitraryComponent>, ArbitraryView {

    @Inject
    lateinit var lazyPresenter: Lazy<ArbitraryPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val viewPagerDelegate by lazy { ViewPagerDelegate(this) }

    override fun getComponent(): ArbitraryComponent =
        ArbitraryComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRoot() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            viewPagerDelegate.onCreate(
                view = viewPager,
                adapter = FragmentStatePagerAdapter(this@ArbitraryFragment),
                savedInstanceState = savedInstanceState
            )

            val pageForContinue = if (presenter.isDiscountWithPercent) PERCENT_PAGE else SUM_PAGE
            viewPagerDelegate.setCurrentPage(pageForContinue)
            tabLayout.setupWithViewPager(viewPager)
            cancelButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            proceedButton.setOnClickListener { presenter.getDiscountArbitraryResult() }
        }

    }

    companion object {
        private const val PERCENT_PAGE = 1
        private const val SUM_PAGE = 0
        fun newInstance() =
            ArbitraryFragment().withArguments()
    }
}