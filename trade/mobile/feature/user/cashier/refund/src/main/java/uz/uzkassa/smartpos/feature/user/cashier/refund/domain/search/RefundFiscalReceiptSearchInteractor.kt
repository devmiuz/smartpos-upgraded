package uz.uzkassa.smartpos.feature.user.cashier.refund.domain.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.ReceiptRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

internal class RefundFiscalReceiptSearchInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptRepository: ReceiptRepository,
    private val refundInteractor: RefundInteractor
) {

    fun getReceiptByFiscalUrl(url: String): Flow<Result<Receipt>> {
        val fiscalUrl: String = URLDecoder.decode(url, StandardCharsets.UTF_8.name())
        return receiptRepository.getReceiptByFiscalUrl(fiscalUrl)
            .catch {
                throw if (it is NotFoundHttpException) FiscalReceiptNotFoundException() else it
            }
            .onEach { refundInteractor.setReceiptForRefund(it) }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}