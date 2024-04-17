package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyBusinessTypeResponse(
    @SerialName("code")
    val code: String,

    @SerialName("nameRu")
    val nameRu: String
)