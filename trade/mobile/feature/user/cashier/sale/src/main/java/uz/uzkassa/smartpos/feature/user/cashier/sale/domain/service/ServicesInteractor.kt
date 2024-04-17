package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.service.Service
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.service.ServiceRepository
import javax.inject.Inject

internal class ServicesInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val serviceRepository: ServiceRepository
) {

    fun getAvailableServices(): Flow<Result<List<Service>>> =
        serviceRepository
            .getServices()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}