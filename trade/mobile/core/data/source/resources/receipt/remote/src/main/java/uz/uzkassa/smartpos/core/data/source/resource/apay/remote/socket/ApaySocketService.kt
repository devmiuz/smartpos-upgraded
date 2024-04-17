package uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket

import com.tinder.scarlet.Protocol
import com.tinder.scarlet.Scarlet
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.registry.ApaySocketServicesRegistry

interface ApaySocketService {

    fun onApayDataReceived(): Flow<String>

    companion object {

        fun instantiate(
            deviceInfoManager: DeviceInfoManager,
            scarlet: Scarlet,
            scarletConfiguration: Scarlet.Configuration,
            scarletProtocol: (destination: String) -> Protocol
        ): ApaySocketService {
            val registry = ApaySocketServicesRegistry(
                deviceInfoManager = deviceInfoManager,
                scarlet = scarlet,
                scarletConfiguration = scarletConfiguration,
                scarletProtocol = scarletProtocol
            )

            return ApaySocketServiceImpl(
                deviceInfoManager = deviceInfoManager,
                apaySocketService = registry.registerApayService()
            )
        }
    }
}