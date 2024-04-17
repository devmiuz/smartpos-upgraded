package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal

import com.tinder.scarlet.Event
import com.tinder.scarlet.ws.Receive
import kotlinx.coroutines.flow.Flow

internal interface ReceiptUpdateSocketServiceInternal {

    @Receive
    fun checkEvent(): Flow<Event>
}