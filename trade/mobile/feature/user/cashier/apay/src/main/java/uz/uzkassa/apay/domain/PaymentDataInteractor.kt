package uz.uzkassa.apay.domain

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.apay.data.repository.qr.card.CardReaderManager
import uz.uzkassa.apay.data.repository.qr.card.PaymentRepository
import uz.uzkassa.apay.data.repository.qr.card.PaymentRepositoryImpl
import uz.uzkassa.apay.data.repository.qr.card.device.DeviceManagerImpl
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import javax.inject.Inject

class PaymentDataInteractor constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val context: Context
) {
    private val deviceManager = DeviceManagerImpl(context)
    private val paymentRepository = PaymentRepositoryImpl(
        deviceManager, CardReaderManager(context, deviceManager)
    )

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val stateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun getCardSlot() =
        paymentRepository.isDeviceCardPortBusy()
            .flowOn(coroutineContextManager.ioContext)
            .flatMapResult()

    fun isPaymentAllowed(): Flow<Boolean> {
        return stateFlow
    }

    fun getCardInfo(isAllowed: Boolean) =
        paymentRepository.getCardInfo(isAllowed)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

}

