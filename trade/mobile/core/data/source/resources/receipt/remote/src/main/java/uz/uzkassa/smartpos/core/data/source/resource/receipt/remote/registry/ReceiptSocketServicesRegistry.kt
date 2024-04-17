package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.registry

import com.tinder.scarlet.Protocol
import com.tinder.scarlet.Scarlet
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal.ReceiptCreateSocketServiceInternal
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal.ReceiptDraftsSocketServiceInternal
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal.ReceiptUpdateSocketServiceInternal

internal class ReceiptSocketServicesRegistry(
    private val deviceInfoManager: DeviceInfoManager,
    private val scarlet: Scarlet,
    private val scarletConfiguration: Scarlet.Configuration,
    private val scarletProtocol: (destination: String) -> Protocol
) {
    private val deviceInfo: DeviceInfo by lazy { deviceInfoManager.deviceInfo }

    fun registerReceiptCreateSocketService(): ReceiptCreateSocketServiceInternal =
        create("/topic/receipt_created")

    fun registerReceiptDraftsSocketService(): ReceiptDraftsSocketServiceInternal =
        create("/topic/receipt_drafts/${deviceInfo.deviceName}/${deviceInfo.serialNumber}")

    fun registerReceiptUpdateSocketService(): ReceiptUpdateSocketServiceInternal =
        create("/topic/receipt_updates/${deviceInfo.deviceName}/${deviceInfo.serialNumber}")

    private inline fun <reified T : Any> create(destination: String): T {
        val configuration: Scarlet.Configuration = scarletConfiguration.copy()
        val protocol: Protocol = scarletProtocol(destination)
        return scarlet.child(protocol, configuration).create()
    }
}