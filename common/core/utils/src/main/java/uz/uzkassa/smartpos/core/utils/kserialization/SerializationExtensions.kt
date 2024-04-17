package uz.uzkassa.smartpos.core.utils.kserialization

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonElementSerializer
import kotlinx.serialization.json.JsonInput
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> SerializationStrategy<T>.asJsonElement(format: Json = Json.actual, obj: T) =
    with(Json) { format.parseJson(stringify(this@asJsonElement, obj)) }

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> SerializationStrategy<T>.asJsonElement(obj: T) =
    asJsonElement(Json.actual, obj)

fun Decoder.decodeJson(): JsonElement =
    (this as JsonInput).decodeJson()

fun Encoder.encodeJson(jsonElement: JsonElement) =
    encode(JsonElementSerializer, jsonElement)