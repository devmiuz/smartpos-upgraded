package uz.uzkassa.smartpos.trade.manager.device

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager

class DeviceInfoManagerProviderImpl(context: Context) : DeviceInfoManagerProvider {

    override val deviceInfoManager by lazy {
        DeviceInfoManager.instantiate(context)
    }
}