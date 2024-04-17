package uz.uzkassa.smartpos.core.data.source.resource.company.vat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyVATResponse(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("percent")
    val percent: Double
)