package uz.uzkassa.smartpos.feature.helper.payment.discount.domain

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.card.model.Card
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass.CardClass
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.result.mapFailure
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.exception.CardIsNotDiscountException
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.exception.CardNotFoundException
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.repository.CardRepository
import javax.inject.Inject

internal class DiscountCardInteractor @Inject constructor(
    private val cardRepository: CardRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var number: String? = null
    
    fun getDiscountCard(): Flow<Result<Card?>> = when {
        number == null || checkNotNull(number).length <= 5 ->
            flowOf(Result.failure(RuntimeException()))
        else ->
            cardRepository
                .getCardByCardNumber(checkNotNull(number))
                .onEach {
                    if (it == null)
                        throw CardNotFoundException()
                    else if (it.cardType.cardClass.type != CardClass.Type.DISCOUNT)
                        throw CardIsNotDiscountException()
                }
                .flatMapResult()
                .map { result ->
                    result.mapFailure {
                        return@mapFailure if (it is NotFoundHttpException) CardNotFoundException()
                        else it
                    }.let {
                        return@let when {
                            it.getOrNull() == null -> Result.failure(CardNotFoundException())
                            it.getOrNull()?.cardType?.cardClass?.type != CardClass.Type.DISCOUNT ->
                                Result.failure(CardIsNotDiscountException())
                            else -> it
                        }
                    }
                }
                .flowOn(coroutineContextManager.ioContext)
    }

    fun setCardNumber(cardNumber: String) {
        number = cardNumber
    }
}