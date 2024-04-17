package uz.uzkassa.apay.data.repository.qr.card

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.repository.qr.card.device.DeviceManager

internal class PaymentRepositoryImpl constructor(
    private val deviceManager: DeviceManager,
    private val cardReader: CardReader
) : PaymentRepository {

    override fun isDeviceCardPortBusy(): Flow<Boolean> {
        return deviceManager.isCardPortBusy()
    }

    override fun getCardInfo(isAllowedPayment: Boolean): Flow<CardInfo> {
        return flow {
            val info = cardReader.startReadingCard().dataOrNull()
            emit(
                CardInfo(
                    cardNumber = info?.cardNumber ?: "",
                    cardExpiryDate = info?.cardExpiryDate ?: "",
                    isPaymentAllowed = isAllowedPayment
                )
            )
        }
    }

}