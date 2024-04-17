package uz.uzkassa.smartpos.core.data.source.resource.card.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardResponse

internal class CardRestServiceImpl(
    private val cardRestServiceInternal: CardRestServiceInternal
) : CardRestService {

    override fun getCardById(cardId: Long): Flow<CardResponse> {
        return cardRestServiceInternal.getCardById(cardId)
    }

    override fun getCardByCardNumber(cardNumber: String): Flow<CardResponse> {
        return cardRestServiceInternal.getCardByCardNumber(cardNumber)
    }
}