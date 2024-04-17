package uz.uzkassa.smartpos.feature.helper.payment.discount.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.card.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.mapper.type.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.Card
import uz.uzkassa.smartpos.core.data.source.resource.card.service.CardRestService
import javax.inject.Inject

internal class   CardRepositoryImpl @Inject constructor(
    private val cardEntityDao: CardEntityDao,
    private val cardTypeEntityDao: CardTypeEntityDao,
//    private val customerEntityDao: CustomerEntityDao,
    private val cardRestService: CardRestService
) : CardRepository {

    // TODO: 6/18/20 Customer saving disabled and this needed fixed

    override fun getCardByCardNumber(cardNumber: String): Flow<Card?> {
        return cardRestService
            .getCardByCardNumber(cardNumber)
            .onEach { cardEntityDao.upsert(it.mapToEntity()) }
            .onEach { cardTypeEntityDao.upsert(it.cardType.mapToEntity()) }
//            .onEach { it -> it.customer?.let { customerEntityDao.upsert(it.mapToEntity()) } }
            .map { it.map() }
    }
}