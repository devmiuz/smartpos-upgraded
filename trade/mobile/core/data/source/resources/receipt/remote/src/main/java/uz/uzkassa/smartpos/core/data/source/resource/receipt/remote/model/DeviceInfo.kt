package uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.model

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal data class DeviceInfo(val model: String, val serialNumber: String) {

    constructor(
        deviceInfo: uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
    ) : this(
        model = deviceInfo.deviceName,
        serialNumber = deviceInfo.serialNumber
    )

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<DeviceInfo> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "DeviceInfoSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: DeviceInfo) {
            val jsonObject: JsonObject = JsonObject.create(
                "model" to JsonPrimitive(value.model),
                "serialNumber" to JsonPrimitive(value.serialNumber)
            )
            encoder.encodeJson(jsonObject)
        }
    }
}