package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.analytics.sales.dynamics.data.model.granularity.Granularity
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales.SalesDynamicsInteractor
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales.wrapper.SalesDynamicsWrapper
import javax.inject.Inject
import kotlin.properties.Delegates

internal class SalesDynamicsPresenter @Inject constructor(
    private val drawerStateDelegate: DrawerStateDelegate,
    private val salesDynamicsInteractor: SalesDynamicsInteractor
) : MvpPresenter<SalesDynamicsView>() {
    private var granularity: Granularity by Delegates.notNull()

    override fun onFirstViewAttach() {
        granularity = Granularity.DAY
        getDaysSalesDynamics()
    }

    fun getDaysSalesDynamics() {
        granularity = Granularity.DAY
        getSalesDynamics()
    }

    fun getWeeksSalesDynamics() {
        granularity = Granularity.WEEK
        getSalesDynamics()
    }

    fun getMonthSalesDynamics() {
        granularity = Granularity.MONTH
        getSalesDynamics()
    }

    fun getSalesDynamics() {
        viewState.onCurrentGranularityChanged(granularity)
        salesDynamicsInteractor.getSalesDynamics(granularity)
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onLoadingSalesDynamics()
                viewState.onHideSalesDynamics()
            }
            .onSuccess {
                viewState.onSuccessSalesDynamics(it)
                if (it.isNotEmpty()) showSalesDynamicsDetails(it.last())
                else viewState.onHideSalesDynamics()
            }
            .onFailure { viewState.onFailureSalesDynamics(it) }
    }

    fun openDrawer() =
        drawerStateDelegate.openDrawer()

    fun showSalesDynamicsDetails(wrapper: SalesDynamicsWrapper) =
        viewState.onShowSalesDynamics(wrapper.salesDynamics)
}