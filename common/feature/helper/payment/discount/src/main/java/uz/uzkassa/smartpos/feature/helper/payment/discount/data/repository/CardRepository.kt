package uz.uzkassa.smartpos.feature.helper.payment.discount.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.card.model.Card

internal interface CardRepository {

    fun getCardByCardNumber(cardNumber: String): Flow<Card?>
}