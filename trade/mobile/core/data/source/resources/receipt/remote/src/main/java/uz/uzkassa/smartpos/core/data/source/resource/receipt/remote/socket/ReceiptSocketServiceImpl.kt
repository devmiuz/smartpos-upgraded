package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket

import com.tinder.scarlet.Event
import com.tinder.scarlet.Event.OnProtocolEvent
import com.tinder.scarlet.Message
import com.tinder.scarlet.ProtocolEvent
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.model.DeviceInfo
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.model.ReceiptListResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal.ReceiptCreateSocketServiceInternal
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal.ReceiptDraftsSocketServiceInternal
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal.ReceiptUpdateSocketServiceInternal
import uz.uzkassa.smartpos.core.manager.logger.Logger
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual

internal class ReceiptSocketServiceImpl constructor(
    private val deviceInfoManager: DeviceInfoManager,
    private val receiptCreateService: ReceiptCreateSocketServiceInternal,
    private val receiptDraftsService: ReceiptDraftsSocketServiceInternal,
    private val receiptUpdateService: ReceiptUpdateSocketServiceInternal
) : ReceiptSocketService {
    private val map: MutableMap<String, ReceiptResponse> = hashMapOf()
    private val deviceInfo by lazy { with(deviceInfoManager) { DeviceInfo(deviceInfo) } }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun onReceiptResponsesReceived(): Flow<List<ReceiptResponse>> {
        return emptyFlow()

//        return flowOf(getReceiptUpdates())
//            .flattenMerge()
//            .map {
//                val responses: MutableList<ReceiptResponse> = arrayListOf()
//                it.forEach { response ->
//                    if (!map.containsKey(response.uid)) {
//                        map[response.uid] = response
//                        responses.add(response)
//                    }
//                }
//                return@map responses
//            }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun getReceiptUpdates(): Flow<List<ReceiptResponse>> {
        return emptyFlow()

//        return receiptUpdateService
//            .checkEvent()
//            .onEach {
//                if (it is Event.OnShouldConnect)
//                    receiptCreateService.send(deviceInfo.asJsonElement())
//            }
//            .filter { it is OnProtocolEvent && it.protocolEvent is ProtocolEvent.OnMessageReceived }
//            .map { (it as OnProtocolEvent).protocolEvent as ProtocolEvent.OnMessageReceived }
//            .map { (it.message as Message.Text).value }
//            .onEach { Logger.wtf("RECEIPT: UPDATES VALUE", it) }
//            .map { Json.actual.parse(ReceiptResponse.serializer(), it) }
//            .filter { it.receiptDetails.isNotEmpty() }
//            .map { listOf(it) }
//            .catch { Logger.wtf("RECEIPT UPDATES: ERROR", it); emit(listOf()) }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun getReceiptDrafts(): Flow<List<ReceiptResponse>> {
        return receiptDraftsService
            .checkEvent()
            .onEach {
                if (it is Event.OnShouldConnect)
                    receiptCreateService.send(deviceInfo.asJsonElement())
            }
            .filter { it is OnProtocolEvent && it.protocolEvent is ProtocolEvent.OnMessageReceived }
            .map { (it as OnProtocolEvent).protocolEvent as ProtocolEvent.OnMessageReceived }
            .map { (it.message as Message.Text).value }
            .onEach { Logger.wtf("RECEIPT: DRAFTS VALUE", it) }
            .map { Json.actual.parse(ReceiptListResponse.serializer(), it).responses }
            .map { it -> it.filter { it.receiptDetails.isNotEmpty() } }
            .catch { Logger.wtf("RECEIPT DRAFTS: ERROR", it); emit(listOf()) }
    }
}