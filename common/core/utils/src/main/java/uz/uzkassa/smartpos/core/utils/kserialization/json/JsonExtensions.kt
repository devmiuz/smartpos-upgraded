package uz.uzkassa.smartpos.core.utils.kserialization.json

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.*

@Suppress("EXPERIMENTAL_API_USAGE")
val Json.Default.actual: Json
    get() = lazyJson

fun <T> Json.toJson(serializer: SerializationStrategy<T>, value: T?): JsonElement =
    if (value == null) JsonNull else toJson(serializer, value)

fun JsonObject.Companion.create(vararg pair: Pair<String, JsonElement?>): JsonObject =
    create(pair.toMap())

fun JsonObject.Companion.create(content: Map<String, JsonElement?>): JsonObject {
    val result: MutableMap<String, JsonElement> = hashMapOf()

    for (entry: Map.Entry<String, JsonElement?> in content) {
        if (entry.value == null || entry.value?.isNull == true) continue
        result[entry.key] = checkNotNull(entry.value)
    }

    return JsonObject(result)
}

private val lazyJson: Json by lazy {
    Json(
        JsonConfiguration.Stable.copy(
            isLenient = true,
            ignoreUnknownKeys = true,
            prettyPrint = true,
            serializeSpecialFloatingPointValues = true,
            useArrayPolymorphism = true
        )
    )
}