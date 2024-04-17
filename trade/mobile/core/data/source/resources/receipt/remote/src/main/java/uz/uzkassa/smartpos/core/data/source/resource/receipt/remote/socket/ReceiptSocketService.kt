package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket

import com.tinder.scarlet.Protocol
import com.tinder.scarlet.Scarlet
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.registry.ReceiptSocketServicesRegistry

interface ReceiptSocketService {

    fun onReceiptResponsesReceived(): Flow<List<ReceiptResponse>>

    companion object {

        fun instantiate(
            deviceInfoManager: DeviceInfoManager,
            scarlet: Scarlet,
            scarletConfiguration: Scarlet.Configuration,
            scarletProtocol: (destination: String) -> Protocol
        ): ReceiptSocketService {
            val registry = ReceiptSocketServicesRegistry(
                deviceInfoManager = deviceInfoManager,
                scarlet = scarlet,
                scarletConfiguration = scarletConfiguration,
                scarletProtocol = scarletProtocol
            )

            return ReceiptSocketServiceImpl(
                deviceInfoManager = deviceInfoManager,
                receiptCreateService = registry.registerReceiptCreateSocketService(),
                receiptDraftsService = registry.registerReceiptDraftsSocketService(),
                receiptUpdateService = registry.registerReceiptUpdateSocketService()
            )
        }
    }
}