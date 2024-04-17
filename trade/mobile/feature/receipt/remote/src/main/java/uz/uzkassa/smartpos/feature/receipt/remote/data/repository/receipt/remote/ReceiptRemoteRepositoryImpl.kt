package uz.uzkassa.smartpos.feature.receipt.remote.data.repository.receipt.remote

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.ReceiptSocketService
import javax.inject.Inject

internal class ReceiptRemoteRepositoryImpl @Inject constructor(
    private val receiptDraftEntityDao: ReceiptDraftEntityDao,
    private val receiptEntityDao: ReceiptEntityDao
//    ,private val receiptSocketService: ReceiptSocketService
) : ReceiptRemoteRepository {

//    @FlowPreview
    override fun subscribe(): Flow<Unit> {
        return flowOf(Unit)
//        return receiptSocketService
//            .onReceiptResponsesReceived()
//            .flatMapMerge { it.asFlow() }
//            .filterNot { receiptDraftEntityDao.isExistsByUid(it.uid) }
//            .onEach { receiptDraftEntityDao.save(ReceiptDraftEntity(it.uid, "", true), it) }
//            .map { Unit }
    }
}