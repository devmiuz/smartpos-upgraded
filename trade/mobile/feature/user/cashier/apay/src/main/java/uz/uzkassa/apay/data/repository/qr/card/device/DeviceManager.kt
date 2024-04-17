package uz.uzkassa.apay.data.repository.qr.card.device

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.apay.data.model.DeviceInfo

interface DeviceManager {


    fun isCardPortBusy(): Flow<Boolean>


    val deviceInfo: DeviceInfo
}