package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.providers.PaymentProvidersRepository
import javax.inject.Inject

internal class PaymentProvidersInteractor @Inject constructor(
    private val providersRepository: PaymentProvidersRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getProviders(): Flow<Result<List<PaymentProvider>>> =
        providersRepository
            .getProviders()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}