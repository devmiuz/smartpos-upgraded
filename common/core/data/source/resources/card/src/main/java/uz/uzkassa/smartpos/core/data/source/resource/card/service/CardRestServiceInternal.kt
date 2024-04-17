package uz.uzkassa.smartpos.core.data.source.resource.card.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardResponse

internal interface CardRestServiceInternal {

    @GET("$API_CUSTOMERS_CARD/{$PATH_ID}")
    fun getCardById(@Path(PATH_ID) cardId: Long): Flow<CardResponse>

    @GET("$API_CUSTOMERS_CARD_NUMBER/{$PATH_ID}")
    fun getCardByCardNumber(@Path(PATH_ID) cardNumber: String): Flow<CardResponse>

    private companion object {
        const val API_CUSTOMERS_CARD: String = "api/cards"
        const val API_CUSTOMERS_CARD_NUMBER: String = "api/cards/number"
        const val PATH_ID: String = "id"
    }
}