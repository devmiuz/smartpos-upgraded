package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal

import com.tinder.scarlet.Event
import com.tinder.scarlet.ws.Receive
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse

internal interface ReceiptDraftsSocketServiceInternal {

    @Receive
    fun checkEvent(): Flow<Event>

    @Receive
    fun receiveReceipt(): Flow<ReceiptResponse>
}