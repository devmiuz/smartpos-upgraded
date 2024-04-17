package uz.uzkassa.smartpos.core.data.source.resource.card.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardResponse

interface CardRestService {

    fun getCardById(cardId: Long): Flow<CardResponse>

    fun getCardByCardNumber(cardNumber: String): Flow<CardResponse>

    companion object {

        fun instantiate(retrofit: Retrofit): CardRestService =
            CardRestServiceImpl(retrofit.create())
    }
}