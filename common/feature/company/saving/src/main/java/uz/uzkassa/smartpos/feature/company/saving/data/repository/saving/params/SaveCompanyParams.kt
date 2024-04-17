package uz.uzkassa.smartpos.feature.company.saving.data.repository.saving.params

import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityResponse
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionResponse
import uz.uzkassa.smartpos.core.utils.kserialization.asJsonElement
import uz.uzkassa.smartpos.core.utils.kserialization.encodeJson
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal data class SaveCompanyParams internal constructor(
    val companyId: Long? = null,
    val ownerFullName: FullNameResponse,
    val activityTypes: List<ActivityTypeResponse>,
    val companyBusinessType: CompanyBusinessTypeResponse,
    val city: CityResponse,
    val region: RegionResponse,
    val vatPercent: Double? = null,
    val tin: Long? = null,
    val name: String,
    val address: String,
    val userId: Long? = null
) {

    constructor(
        ownerFirstName: String,
        ownerLastName: String?,
        ownerPatronymic: String?,
        activityTypes: List<ActivityType>,
        companyBusinessType: CompanyBusinessType,
        city: City,
        region: Region,
        vatPercent: Double?,
        tin: Long?,
        name: String,
        address: String
    ) : this(
        companyId = null,
        ownerFullName = FullNameResponse(ownerFirstName, ownerLastName, ownerPatronymic),
        activityTypes = activityTypes.mapToResponses(),
        companyBusinessType = companyBusinessType.mapToResponse(),
        city = city.mapToResponse(),
        region = region.mapToResponse(),
        vatPercent = vatPercent,
        tin = tin,
        name = name,
        address = address,
        userId = null
    )

    fun asJsonElement(): JsonElement =
        asJsonElement(this)

    companion object : SerializationStrategy<SaveCompanyParams> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "SaveCompanyParamsSerializer",
            kind = PrimitiveKind.STRING
        )

        @Suppress("EXPERIMENTAL_API_USAGE")
        override fun serialize(encoder: Encoder, value: SaveCompanyParams) {
            val json: Json = Json.actual
            val jsonObject: JsonObject =
                JsonObject.create(
                    "id" to JsonPrimitive(value.companyId),
                    "contactFullName" to json.toJson(
                        serializer = FullNameResponse.serializer(),
                        value = value.ownerFullName
                    ),
                    "types" to json.toJson(
                        serializer = ListSerializer(ActivityTypeResponse.serializer()),
                        value = value.activityTypes
                    ),
                    "businessType" to json.toJson(
                        serializer = CompanyBusinessTypeResponse.serializer(),
                        value = value.companyBusinessType
                    ),
                    "city" to json.toJson(CityResponse.serializer(), value.city),
                    "region" to json.toJson(RegionResponse.serializer(), value.region),
                    "paysNds" to JsonPrimitive(value.vatPercent != null && value.vatPercent > 0.0),
                    "ndsPercent" to JsonPrimitive(value.vatPercent),
                    "tin" to JsonPrimitive(value.tin),
                    "name" to JsonPrimitive(value.name),
                    "address" to JsonPrimitive(value.address),
                    "userId" to JsonPrimitive(value.userId)
                )

            encoder.encodeJson(jsonObject)
        }
    }
}