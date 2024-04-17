package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamics
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.utils.math.toStringCompat
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.analytics.sales.dynamics.data.model.granularity.Granularity
import uz.uzkassa.smartpos.feature.supervisior.dashboard.databinding.FragmentSalesDynamicsBinding as ViewBinding
import uz.uzkassa.smartpos.feature.supervisior.dashboard.R
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales.wrapper.SalesDynamicsWrapper
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.delegate.di.SalesDynamicsComponent
import javax.inject.Inject

class SalesDynamicsFragment : MvpAppCompatFragment(R.layout.fragment_sales_dynamics),
    IHasComponent<SalesDynamicsComponent>,
    SalesDynamicsView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<SalesDynamicsPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.showSalesDynamicsDetails(it) }
    }

    override fun getComponent(): SalesDynamicsComponent =
        SalesDynamicsComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { presenter.openDrawer() }
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)

            daysButton.setOnClickListener { presenter.getDaysSalesDynamics() }
            monthsButton.setOnClickListener { presenter.getMonthSalesDynamics() }
            weeksButton.setOnClickListener { presenter.getWeeksSalesDynamics() }
        }
    }

    override fun onLoadingSalesDynamics() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessSalesDynamics(salesDynamics: List<SalesDynamicsWrapper>) =
        recyclerViewDelegate.onSuccess(salesDynamics)

    override fun onFailureSalesDynamics(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable){
            presenter.getSalesDynamics()
        }

    override fun onCurrentGranularityChanged(granularity: Granularity) {
        binding.apply {
            val buttonId: Int = when (granularity) {
                Granularity.DAY -> daysButton.id
                Granularity.MONTH -> monthsButton.id
                Granularity.WEEK -> weeksButton.id
            }
            buttonGroup.check(buttonId)
        }
    }

    override fun onShowSalesDynamics(salesDynamics: SalesDynamics) {
        binding.apply {
            discountValueTextView.text = salesDynamics.discount.toStringCompat()
            paymentCardValueTextValue.text = salesDynamics.salesCard.toStringCompat()
            paymentCashValueTextValue.text = salesDynamics.salesCash.toStringCompat()
            receiptsValueTextView.text = salesDynamics.salesCount.roundToString()
            refundsValueTextView.text = salesDynamics.refund.toStringCompat()
            saleValueTextView.text = salesDynamics.salesTotal.toStringCompat()
            vatValueTextView.text = salesDynamics.vat.toStringCompat()
        }
    }

    override fun onHideSalesDynamics() {
        binding.apply {
            discountValueTextView.text = ""
            paymentCardValueTextValue.text = ""
            paymentCashValueTextValue.text = ""
            receiptsValueTextView.text = ""
            refundsValueTextView.text = ""
            saleValueTextView.text = ""
            vatValueTextView.text = ""
        }
    }

    companion object {

        fun newInstance(): SalesDynamicsFragment =
            SalesDynamicsFragment()
                .withArguments()
    }
}