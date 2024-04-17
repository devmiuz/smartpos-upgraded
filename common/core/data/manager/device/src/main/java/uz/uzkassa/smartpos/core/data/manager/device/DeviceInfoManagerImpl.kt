package uz.uzkassa.smartpos.core.data.manager.device

import android.content.Context
import uz.uzkassa.engine.device.integration.common.response.EngineResult
import uz.uzkassa.engine.device.integration.deviceinfo.DeviceInfoIntegrationApi
import uz.uzkassa.engine.device.integration.deviceinfo.response.DeviceLocation
import uz.uzkassa.engine.device.integration.deviceinfo.response.DeviceModel
import uz.uzkassa.engine.device.integration.deviceinfo.response.DeviceSerialNumber
import uz.uzkassa.smartpos.core.data.manager.device.exception.DeviceInfoException
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceLocationInfo

internal class DeviceInfoManagerImpl(context: Context) : DeviceInfoManager {
    private val integrationApi by lazy { DeviceInfoIntegrationApi(context) }

    override val deviceInfo: DeviceInfo by lazy {
        val deviceModelResult: EngineResult<DeviceModel> = integrationApi.getDeviceModel()
        val serialNumberResult: EngineResult<DeviceSerialNumber> =
            integrationApi.getDeviceSerialNumber()

        val deviceName: String = deviceModelResult.dataOrNull()?.model
            ?: throwException(deviceModelResult)
        val serialNumber: String = serialNumberResult.dataOrNull()?.serialNumber
            ?: throwException(serialNumberResult)

        return@lazy DeviceInfo(deviceName, serialNumber)
    }

    override val locationInfo: DeviceLocationInfo?
        get() {
            val result: EngineResult<DeviceLocation> = integrationApi.getDeviceLocation()
            return result.dataOrNull()?.let { DeviceLocationInfo(it.latitude, it.longitude) }
        }

    private fun <T> throwException(result: EngineResult<*>): T =
        throw DeviceInfoException(result.errorOrThrow())
}