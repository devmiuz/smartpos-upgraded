package uz.uzkassa.apay.data.repository.qr.card

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.ClientInfo
import uz.uzkassa.apay.data.model.CreatePayment
import java.math.BigDecimal

interface PaymentRepository {

    fun getCardInfo(isAllowedPayment: Boolean): Flow<CardInfo>

    fun isDeviceCardPortBusy(): Flow<Boolean>
}