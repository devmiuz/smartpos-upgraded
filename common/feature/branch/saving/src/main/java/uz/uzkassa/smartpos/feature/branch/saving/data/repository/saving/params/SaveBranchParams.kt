package uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.kserialization.json.create
import uz.uzkassa.smartpos.core.utils.kserialization.json.toJson

internal data class SaveBranchParams(
    val id: Long?,
    val activityType: ActivityType? = null,
    val name: String,
    val region: Region? = null,
    val city: City? = null,
    val address: String
) {

    constructor(
        activityType: ActivityType?,
        name: String,
        region: Region?,
        city: City?,
        address: String
    ) : this(
        id = null,
        activityType = activityType,
        name = name,
        region = region,
        city = city,
        address = address
    )

    fun asJsonElement() =
        asJsonElement(this)

    companion object : SerializationStrategy<SaveBranchParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "SaveBranchParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: SaveBranchParams) {
            val json: Json = Json.actual

            val jsonObject: JsonObject = JsonObject.create(
                "id" to JsonPrimitive(value.id),
                "activityTypeDTO" to json.toJson(
                    ActivityTypeResponse.serializer(),
                    value.activityType?.mapToResponse()
                ),
                "name" to JsonPrimitive(value.name),
                "region" to json.toJson(
                    RegionResponse.serializer(),
                    value.region?.mapToResponse()
                ),
                "city" to json.toJson(
                    CityResponse.serializer(),
                    value.city?.mapToResponse()
                ),
                "address" to JsonPrimitive(value.address)
            )

            encoder.encodeJson(jsonObject)
        }
    }
}