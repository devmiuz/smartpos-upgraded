package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.service.Service

internal interface ServicesView : MvpView {

    fun onLoadingAvailableServices()

    fun onSuccessAvailableServices(services: List<Service>)

    fun onErrorAvailableServices(throwable: Throwable)
    
    @OneExecution
    fun onErrorOpenService(throwable: Throwable)
}