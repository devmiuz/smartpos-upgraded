package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductPackageTypeResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("code")
    val code: Long,

    @SerialName("checksum")
    val checksum: Int = 0,

    @SerialName("countable")
    val isCountable: Boolean,

    @SerialName("name")
    val name: String,

    @SerialName("nameRu")
    val nameRu: String? = "",

    @SerialName("nameUz")
    val nameUz: String,

    @SerialName("description")
    val description: String
)