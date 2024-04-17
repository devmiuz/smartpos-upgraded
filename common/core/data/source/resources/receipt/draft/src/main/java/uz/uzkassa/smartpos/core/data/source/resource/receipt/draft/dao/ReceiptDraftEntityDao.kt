package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse

@Dao
abstract class ReceiptDraftEntityDao(
    database: SupportRoomDatabase
) : BaseDao.Impl<ReceiptDraftEntity>() {
    private val receiptEntityDao = database.getDao<ReceiptEntityDao>()

    @Query(value = "SELECT COUNT(receipt_draft_receipt_uid) FROM receipt_drafts WHERE receipt_draft_receipt_uid = :uid")
    internal abstract fun isReceiptDraftExistsByUid(uid: String): Boolean

    @Query(value = "SELECT * FROM RECEIPT_DRAFTS WHERE receipt_draft_receipt_uid=:uid")
    abstract fun getDraftByUid(uid: String): ReceiptDraftEntity

    @Query(value = "DELETE FROM receipt_drafts WHERE receipt_draft_id = :id")
    abstract fun deleteById(id: Long)

    @Query(value = "DELETE FROM receipt_drafts WHERE receipt_draft_is_remote = 1")
    abstract fun deleteRemoteDraftReceipts()

    @Query(value = "DELETE FROM receipt_drafts WHERE receipt_draft_receipt_uid IN(:receiptUIds)")
    abstract fun deleteSyncedDrafts(receiptUIds: List<String>)

    fun isExistsByUid(uid: String): Boolean {
        return isReceiptDraftExistsByUid(uid) || receiptEntityDao.isExistsByUid(uid)
    }

    private fun isDraft(uid: String): Boolean {
        return receiptEntityDao.isDraftReceipt(uid)
    }

    private fun isValidDraft(uid: String): Boolean {
        return !isExistsByUid(uid) || isDraft(uid)
    }

    fun save(draftId: Long?, entity: ReceiptDraftEntity, receipt: Receipt) {
        if (draftId != null) {
            deleteById(draftId)
            receiptEntityDao.deleteByReceiptUid(receipt.uid)
        }
        receiptEntityDao.save(receipt)
        insertOrUpdate(entity)
    }

    fun save(entity: ReceiptDraftEntity, response: ReceiptResponse) {
        insertOrUpdate(entity)
        receiptEntityDao.save(response)
    }

    fun save(entities: List<ReceiptDraftEntity>, responses: List<ReceiptResponse>) {
        val filteredEntities = entities.filter { isValidDraft(it.receiptUid) }
        val filteredResponse = responses.filter { !isExistsByUid(it.uid) }
        insertOrUpdate(filteredEntities)
        receiptEntityDao.save(filteredResponse)
    }
}