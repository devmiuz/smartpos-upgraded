package uz.uzkassa.apay.data.model.card_list

import kotlinx.serialization.Serializable

@Serializable
data class CardListResponse(
    val cardBackground: String?,
    val cardMiniBackground: String?,
    val id: String,
    val name: String,
    val number: String,
    val bankLogo: String?
)

@Serializable
data class CardData(
    val background: String?,
    val logo: String?
)
