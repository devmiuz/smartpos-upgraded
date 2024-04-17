package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamics
import uz.uzkassa.smartpos.feature.analytics.sales.dynamics.data.model.granularity.Granularity
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales.wrapper.SalesDynamicsWrapper

internal interface SalesDynamicsView : MvpView {

    fun onLoadingSalesDynamics()

    fun onSuccessSalesDynamics(salesDynamics: List<SalesDynamicsWrapper>)

    fun onFailureSalesDynamics(throwable: Throwable)

    fun onCurrentGranularityChanged(granularity: Granularity)

    fun onShowSalesDynamics(salesDynamics: SalesDynamics)

    fun onHideSalesDynamics()
}