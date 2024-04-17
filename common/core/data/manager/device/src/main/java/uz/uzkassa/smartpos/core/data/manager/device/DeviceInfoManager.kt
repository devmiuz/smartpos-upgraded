package uz.uzkassa.smartpos.core.data.manager.device

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceLocationInfo

interface DeviceInfoManager {

    val deviceInfo: DeviceInfo

    val locationInfo: DeviceLocationInfo?

    companion object {

        fun instantiate(context: Context): DeviceInfoManager =
            DeviceInfoManagerImpl(context)
    }
}