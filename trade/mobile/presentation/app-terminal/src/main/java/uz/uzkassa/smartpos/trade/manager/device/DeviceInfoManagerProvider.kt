package uz.uzkassa.smartpos.trade.manager.device

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager

interface DeviceInfoManagerProvider {

    val deviceInfoManager: DeviceInfoManager

    companion object {

        fun instantiate(context: Context): DeviceInfoManagerProvider =
            DeviceInfoManagerProviderImpl(context)
    }
}