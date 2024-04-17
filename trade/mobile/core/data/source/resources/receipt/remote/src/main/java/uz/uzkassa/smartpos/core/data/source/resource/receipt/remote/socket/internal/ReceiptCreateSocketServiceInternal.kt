package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.internal
import com.tinder.scarlet.ws.Send
import kotlinx.serialization.json.JsonElement

internal interface ReceiptCreateSocketServiceInternal {

    @Send
    fun send(jsonElement: JsonElement): Boolean
}