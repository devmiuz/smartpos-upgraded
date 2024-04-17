package uz.uzkassa.smartpos.feature.user.cashier.refund.domain.search

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.ReceiptUidNotDefinedException
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.ReceiptRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import javax.inject.Inject

internal class RefundReceiptSearchInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptRepository: ReceiptRepository,
    private val refundInteractor: RefundInteractor
) {
    private var uid: String? = null

    fun setUid(value: String) {
        uid = TextUtils.replaceAllLetters(value)
    }

    fun getReceiptByUid(): Flow<Result<Receipt>> {
        return when {
            uid.isNullOrBlank() -> flowOf(Result.failure(ReceiptUidNotDefinedException()))
            else -> receiptRepository.getReceiptByUid(checkNotNull(uid))
                .catch { throw if (it is NotFoundHttpException) FiscalReceiptNotFoundException() else it }
                .onEach { refundInteractor.setReceiptForRefund(it) }
                .flatMapResult()
                .flowOn(coroutineContextManager.ioContext)
        }
    }
}