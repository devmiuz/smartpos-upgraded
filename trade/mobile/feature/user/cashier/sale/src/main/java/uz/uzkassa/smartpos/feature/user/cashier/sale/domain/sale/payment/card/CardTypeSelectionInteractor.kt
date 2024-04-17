package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.card

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.payment.card.CardTypeRepository
import javax.inject.Inject

internal class CardTypeSelectionInteractor @Inject constructor(
    private val cardTypeRepository: CardTypeRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getCardType(): Flow<Result<List<ReceiptPayment.Type>>> {
        return cardTypeRepository.getCartTypes()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}