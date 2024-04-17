package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.credit_advance

import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToReceiptResonses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.utils.math.sum
import javax.inject.Inject

internal class CreditAdvanceRepositoryImpl @Inject constructor(
    private val receiptRelationDao: ReceiptRelationDao,
    private val receiptRestService: ReceiptRestService,
    private val receiptEntityDao: ReceiptEntityDao,
    private val companyRelationDao: CompanyRelationDao
) : CreditAdvanceRepository {

    override fun getAdvanceReceipts(): Flow<List<Receipt>> {
        return receiptRelationDao
            .getAdvanceReceiptsRelations()
            .map { it.map() }
            .map {
                val fullReceiptList = it.toList()
                val uniqueReceiptList = it.distinctBy { receipt -> receipt.originUid }

                uniqueReceiptList.forEach { receipt ->
                    val totalPaid = fullReceiptList.filter { it.originUid == receipt.originUid }
                        .map { it.totalPaid }.sum()
                    receipt.totalPaid = totalPaid
                }

                return@map uniqueReceiptList
            }
    }

    override fun getCreditReceipts(): Flow<List<Receipt>> {
        return receiptRelationDao
            .getCreditReceiptsRelations()
            .map { it.map() }
            .map {
                val fullReceiptList = it.toList()
                val uniqueReceiptList = it.distinctBy { receipt -> receipt.originUid }

                uniqueReceiptList.forEach { receipt ->
                    val totalPaid = fullReceiptList.filter { it.originUid == receipt.originUid }
                        .map { it.totalPaid }.sum()
                    receipt.totalPaid = totalPaid
                }

                return@map uniqueReceiptList
            }
    }

    override fun getAdvanceReceiptByUid(uid: String): Flow<Receipt> {
        return flow {
            emit(receiptRelationDao.getRelationByUid(uid))
        }
            .map { it }
            .catch {
                val relationFlow: Flow<ReceiptRelation> = receiptRestService
                    .getReceiptByUid(uid)
                    .onEach {
                        receiptEntityDao.save(it)
                    }
                    .map { receiptRelationDao.getRelationByUid(uid) }
                emitAll(relationFlow)
            }
            .map {
                it.map()
            }
    }

    @OptIn(FlowPreview::class)
    override fun searchCreditAdvanceReceipts(
        customerName: String,
        customerPhone: String,
        fiscalUrl: String,
        receiptUid: String
    ): Flow<List<Receipt>> {

        var isFirstFilter = true
        var filtersCount = 0

        var queryString = "SELECT * FROM receipts WHERE "

        if (customerName.isNotEmpty()) {
            filtersCount += 1
            if (isFirstFilter) {
                isFirstFilter = false
            } else {
                queryString += " OR "
            }
            queryString += "receipt_customer_name = '$customerName'"
        }

        if (customerPhone.isNotEmpty()) {
            filtersCount += 1
            if (isFirstFilter) {
                isFirstFilter = false
            } else {
                queryString += " OR "
            }
            queryString += "receipt_customer_contact = '$customerPhone'"
        }

        if (fiscalUrl.isNotEmpty()) {
            filtersCount += 1
            if (isFirstFilter) {
                isFirstFilter = false
            } else {
                queryString += " OR "
            }
            queryString += "receipt_fiscal_url = '$fiscalUrl' AND receipt_origin_uid = receipt_uid"
        }

        if (receiptUid.isNotEmpty()) {
            filtersCount += 1
            if (isFirstFilter) {
                isFirstFilter = false
            } else {
                queryString += " OR "
            }
            queryString += "receipt_origin_uid = '$receiptUid'"
        }

        if (filtersCount > 0) {
            queryString += " ORDER BY receipt_date DESC"
        } else {
            return flowOf(emptyList())
        }

        val query = SimpleSQLiteQuery(queryString)

        return receiptRelationDao.getRelationByFilter(query = query)
            .flatMapConcat { receiptRelations ->
                if (receiptRelations.isEmpty()) {
                    val companyId: Long = companyRelationDao.getRelation().companyEntity.id

                    var filters = "api/receipt/credits?companyId=${companyId}"
                    if (receiptUid.isNotEmpty()) {
                        filters += "&uid=${receiptUid}"
                    }
                    if (fiscalUrl.isNotEmpty()) {
                        val formattedUrl: String = "\"$fiscalUrl\"".replace("&", "%26")
                        filters += "&fiscalUrl=${formattedUrl}"
                    }
                    if (customerName.isNotEmpty()) {
                        filters += "&customerName=${customerName}"
                    }
                    if (customerPhone.isNotEmpty()) {
                        filters += "&customerPhone=${customerPhone}"
                    }

                    val remoteReceipts: Flow<List<ReceiptRelation>> = receiptRestService
                        .searchCreditAdvanceReceipt(
                            filters = filters
                        )
                        .onEach {
                            if (it.isNotEmpty()) {
                                receiptEntityDao.saveWithBaseStatus(
                                    responses = it.mapToReceiptResonses(),
                                    baseStatus = ReceiptStatus.valueOf(it[0].childResponses[0].status.name)
                                )
                            }
                        }
                        .flatMapConcat {
                            receiptRelationDao.getRelationByFilter(query = query)
                        }
                    return@flatMapConcat remoteReceipts
                } else {
                    flowOf(receiptRelations)
                }
            }
            .map {
                it.map()
            }
    }

}