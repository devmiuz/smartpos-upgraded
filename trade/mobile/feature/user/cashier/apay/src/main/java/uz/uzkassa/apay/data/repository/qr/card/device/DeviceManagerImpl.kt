package uz.uzkassa.apay.data.repository.qr.card.device

import android.content.Context
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.apay.data.model.DeviceInfo
import uz.uzkassa.apay.data.repository.qr.card.device.info.NexGoDeviceInfo
import java.util.*
import javax.inject.Inject
import uz.uzkassa.apay.data.repository.qr.card.device.info.DeviceInfo as CurrentDeviceInfo

internal class DeviceManagerImpl constructor(context: Context) : DeviceManager {
    private val currentDeviceInfo: CurrentDeviceInfo = when {
        NexGoDeviceInfo.isAvailable -> NexGoDeviceInfo(context)
        else -> Companion
    }

    override val deviceInfo: DeviceInfo by lazy {
        with(currentDeviceInfo) {
            DeviceInfo(
                deviceModel = deviceModel,
                serialNumber = serialNumber
            )
        }
    }

    override fun isCardPortBusy(): Flow<Boolean> {
        return currentDeviceInfo.isCardSlotBusy()
    }

    private companion object : CurrentDeviceInfo {
        override val deviceModel: String = Build.MODEL
        override val serialNumber: String = ""
        override fun uninstallApp(packageName: String): Flow<Unit> = flowOf(Unit)
        override fun updateTime(date: Date) {}
        override fun isCardSlotBusy(): Flow<Boolean> {
            return flowOf(false)
        }
    }
}