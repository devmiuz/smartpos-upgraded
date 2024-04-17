package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.adapter.message

import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerByTypeToken
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import java.lang.reflect.Type

class KotlinSerializerMessageAdapter<T : Any> private constructor(
    private val type: Type
) : MessageAdapter<T> {

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val json: Json = Json.actual

    override fun fromMessage(message: Message): T {
        val value = when (message) {
            is Message.Text -> message.value
            is Message.Bytes -> String(message.value)
        }

        return json.parse(getSerializer(), value)
    }

    override fun toMessage(data: T): Message {
        val stringValue: String = json.stringify(getSerializer(), data)
        return Message.Text(stringValue)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSerializer(): KSerializer<T> =
        serializerByTypeToken(type) as KSerializer<T>

    companion object : MessageAdapter.Factory {

        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> =
            KotlinSerializerMessageAdapter<Any>(type)
    }
}