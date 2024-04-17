package uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.internal

import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement

internal interface ApaySocketServiceInternal {

    @Receive
    fun receiveApayData(): Flow<String>
}

