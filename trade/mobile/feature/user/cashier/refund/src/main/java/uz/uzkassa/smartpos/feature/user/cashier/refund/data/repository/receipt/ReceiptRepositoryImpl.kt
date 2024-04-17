package uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotAllowedException
import javax.inject.Inject

internal class ReceiptRepositoryImpl @Inject constructor(
    private val receiptEntityDao: ReceiptEntityDao,
    private val receiptRelationDao: ReceiptRelationDao,
    private val receiptRestService: ReceiptRestService
) : ReceiptRepository {

    @FlowPreview
    override fun getReceiptByUid(uid: String): Flow<Receipt> {
        return flow { emit(receiptRelationDao.getRelationByUid(uid)) }
            .onEach {
                if (it.receiptEntity.originUid != null) {
                    throw FiscalReceiptNotAllowedException()
                }
            }
            .catch { it ->
                if (it is FiscalReceiptNotAllowedException) throw it
                else {
                    val relationFlow: Flow<ReceiptRelation> =
                        receiptRestService.getReceiptByUid(uid)
                            .onEach {
                                if (it.originUid != null)
                                    throw FiscalReceiptNotAllowedException()
                            }
                            .onEach {
                                receiptEntityDao.save(it) }
                            .map { receiptRelationDao.getRelationByUid(uid) }
                    emitAll(relationFlow)
                }
            }
            .map { it.map() }
    }

    @FlowPreview
    override fun getReceiptByFiscalUrl(url: String): Flow<Receipt> {
        return flow { emit(receiptRelationDao.getRelationByFiscalUrl(url)) }
            .onEach {
                if (!it.receiptEntity.originUid.isNullOrEmpty())
                    throw FiscalReceiptNotAllowedException()
            }
            .catch { it ->
                if (it is FiscalReceiptNotAllowedException) throw it
                else {
                    val formattedUrl = "\"$url\""
                    val relationFlow: Flow<ReceiptRelation> =
                        receiptRestService.getReceiptByFiscalUrl(formattedUrl)
                            .onEach {
                                if (!it.originUid.isNullOrEmpty())
                                    throw FiscalReceiptNotAllowedException()
                            }
                            .onEach { receiptEntityDao.save(it) }
                            .map { receiptRelationDao.getRelationByFiscalUrl(url) }

                    emitAll(relationFlow)
                }
            }
            .map { it.map() }
    }
}
