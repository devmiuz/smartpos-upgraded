package uz.uzkassa.smartpos.core.data.source.resource.apay.remote.registry

import com.tinder.scarlet.Protocol
import com.tinder.scarlet.Scarlet
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.internal.ApaySocketServiceInternal

internal class ApaySocketServicesRegistry(
    private val deviceInfoManager: DeviceInfoManager,
    private val scarlet: Scarlet,
    private val scarletConfiguration: Scarlet.Configuration,
    private val scarletProtocol: (destination: String) -> Protocol
) {
    private val deviceInfo: DeviceInfo by lazy { deviceInfoManager.deviceInfo }

    fun registerApayService(): ApaySocketServiceInternal =
        create("/topic/public-kkm-aaaaaaaaaaa")

    private inline fun <reified T : Any> create(destination: String): T {
        val configuration: Scarlet.Configuration = scarletConfiguration.copy()
        val protocol: Protocol = scarletProtocol(destination)
        return scarlet.child(protocol, configuration).create()
    }
}