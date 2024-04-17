package uz.uzkassa.smartpos.feature.check.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service.CashTransactionRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.feature.check.data.model.exception.FiscalReceiptNotFoundException
import uz.uzkassa.smartpos.feature.check.data.repository.ReceiptCheckRepository
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureArgs
import java.lang.RuntimeException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*
import javax.inject.Inject

internal class ReceiptCheckInteractor @Inject constructor(
    private val receiptCheckRepository: ReceiptCheckRepository,
    receiptCheckFeatureArgs: ReceiptCheckFeatureArgs,
    private val receiptEntityDao: ReceiptEntityDao,
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptRelationDao: ReceiptRelationDao,
    private val receiptRestService: ReceiptRestService,
    private val cashTransactionRestService: CashTransactionRestService
) {

    fun getReceiptByFiscalUrl(url: String): Flow<Result<Receipt>> {
        val fiscalUrl: String = URLDecoder.decode(url, StandardCharsets.UTF_8.name())
        return receiptCheckRepository.getReceiptByFiscalUrl(fiscalUrl)
            .catch { throw if (it is NotFoundHttpException) FiscalReceiptNotFoundException() else it }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getCompany(companyId: Long): Flow<Result<CompanyResponse>> {
        return receiptCheckRepository.getCompany(companyId)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getUserByUserId(userId: Long): Flow<Result<UserResponse>> {
        return receiptCheckRepository.getUserByUserId(userId)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun createReceipt(receipt: Receipt): Flow<Result<ResponseBody>> {
        return receiptCheckRepository.createReceipt(receipt)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}