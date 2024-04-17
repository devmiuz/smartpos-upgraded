package uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.internal.ApaySocketServiceInternal

internal class ApaySocketServiceImpl constructor(
    private val deviceInfoManager: DeviceInfoManager,
    private val apaySocketService: ApaySocketServiceInternal
) : ApaySocketService {
    private val map: MutableMap<String, ReceiptResponse> = hashMapOf()
    private val deviceInfo by lazy { with(deviceInfoManager) { DeviceInfo(deviceInfo) } }

    override fun onApayDataReceived(): Flow<String> {
        return emptyFlow()
    }
}