package uz.uzkassa.smartpos.feature.check.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service.CashTransactionRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.AdditionalDetails
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.feature.check.data.model.exception.FiscalReceiptNotAllowedException
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureArgs
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject

internal class ReceiptCheckRepositoryImpl @Inject constructor(
    private val receiptCheckFeatureArgs: ReceiptCheckFeatureArgs,
    private val receiptRelationDao: ReceiptRelationDao,
    private val receiptRestService: ReceiptRestService,
    private val receiptEntityDao: ReceiptEntityDao,
    private val companyRestService: CompanyRestService,
    private val userRestService: UserRestService,
    private val cashTransactionRestService: CashTransactionRestService
) : ReceiptCheckRepository {

    @FlowPreview
    override fun getReceiptByFiscalUrl(url: String): Flow<Receipt> {
        return flow { emit(receiptRelationDao.getRelationByFiscalUrl(url)) }
            .onEach {
                with(it.receiptEntity) {
                    if (originUid != null) {
                        if (baseStatus != ReceiptStatus.ADVANCE.name && baseStatus != ReceiptStatus.CREDIT.name) {
                            throw FiscalReceiptNotAllowedException()
                        }
                    }
                }
            }
            .catch { it ->
                if (it is FiscalReceiptNotAllowedException) throw it
                else {
                    val formattedUrl = "\"$url\""
                    val relationFlow: Flow<ReceiptRelation> =
                        receiptRestService.getReceiptByFiscalUrl(formattedUrl)
                            .onEach {
                                if (it.originUid != null)
                                    throw FiscalReceiptNotAllowedException()
                            }
                            .onEach { receiptEntityDao.save(it) }
                            .map { receiptRelationDao.getRelationByFiscalUrl(url) }

                    emitAll(relationFlow)
                }
            }
            .map { it.map() }
    }

    override fun getCompany(companyId: Long): Flow<CompanyResponse> {
        return companyRestService.getCompany(companyId)
    }

    override fun getUserByUserId(userId: Long): Flow<UserResponse> {
        return userRestService.getUserByUserId(userId)
    }

    override fun createReceipt(receipt: Receipt): Flow<ResponseBody> {
        return flow { emit(receiptEntityDao.getByUid(receipt.uid)) }
            .flatMapConcat { receiptEntity ->
                if (receiptEntity == null) throw RuntimeException("Receipt not found")
                val currentDate: Date = Date().let {
                    return@let if (receiptEntity.receiptDate.time > it.time) it
                    else receiptEntity.receiptDate
                }

                val receiptResponse = ReceiptResponse(
                    uid = receiptEntity.uid,
                    originUid = receiptEntity.originUid,
                    userResponse = ReceiptResponse.ReceiptUserResponse(receiptEntity.userId),
                    customerId = receiptEntity.customerId,
                    loyaltyCardId = receiptEntity.loyaltyCardId,
                    shiftId = receiptEntity.shiftId,
                    branchId = receiptEntity.branchId,
                    companyId = receiptEntity.companyId,
                    receiptDate = currentDate,
                    receiptLatitude = receiptEntity.receiptLatitude,
                    receiptLongitude = receiptEntity.receiptLongitude,
                    shiftNumber = receiptEntity.shiftNumber,
                    discountPercent = receiptEntity.discountPercent,
                    totalCard = receiptEntity.totalCard,
                    totalCash = receiptEntity.totalCash,
                    totalCost = receiptEntity.totalCost,
                    totalDiscount = receiptEntity.totalDiscount,
                    totalExcise = receiptEntity.totalExcise,
                    totalLoyaltyCard = receiptEntity.totalLoyaltyCard,
                    totalVAT = receiptEntity.totalVAT,
                    totalPaid = receiptEntity.totalPaid,
                    terminalModel = receiptEntity.terminalModel,
                    terminalSerialNumber = receiptEntity.terminalSerialNumber,
                    fiscalSign = receiptEntity.fiscalSign,
                    fiscalUrl = receiptEntity.fiscalUrl,
                    status = ReceiptStatusResponse.valueOf(receiptEntity.status),
                    receiptDetails = receipt.receiptDetails.mapToResponses(),
                    receiptPayments = receiptRelationDao.getRelationByUid(receipt.uid).receiptPaymentEntities.mapToResponses(),
                    readonly = receiptEntity.readonly,
                    forceToPrint = receiptEntity.forceToPrint,
                    customerName = receiptEntity.customerName,
                    customerContact = receiptEntity.customerContact,
                    additionalDetails = AdditionalDetails(
                        receiptEntity.receiptSeq,
                        receiptEntity.terminalId,
                        receiptEntity.fiscalReceiptCreatedDate,
                        receiptEntity.paymentProviderId,
                        receipt.transactionId
                    ),
                    paymentBillId = receiptEntity.paymentBillId
                )

                val jsonElement: JsonElement = Json.actual.toJson(
                    serializer = ReceiptResponse.serializer(),
                    value = receiptResponse
                )
                cashTransactionRestService.createReceipt(jsonElement)
            }
    }
}