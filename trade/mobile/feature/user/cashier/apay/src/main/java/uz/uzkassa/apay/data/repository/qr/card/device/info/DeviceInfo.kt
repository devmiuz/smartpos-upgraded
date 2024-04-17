package uz.uzkassa.apay.data.repository.qr.card.device.info

import kotlinx.coroutines.flow.Flow
import java.util.*

interface DeviceInfo {

    val deviceModel: String

    val serialNumber: String

    fun uninstallApp(packageName: String): Flow<Unit>

    fun updateTime(date: Date)

    fun isCardSlotBusy(): Flow<Boolean>

    interface Availability {

        val isAvailable: Boolean
    }
}