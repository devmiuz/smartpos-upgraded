package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailRelation

@Dao
abstract class ReceiptDraftRelationDao(
    database: SupportRoomDatabase
) : BaseDao.Impl<ReceiptDraftEntity>() {
    private val receiptDetailRelationDao = database.getDao<ReceiptDetailRelationDao>()

    @Transaction
    @Query(value = "SELECT * FROM receipt_drafts INNER JOIN (SELECT * FROM receipts) receipt ON receipt_uid = receipt_draft_receipt_uid WHERE receipt_draft_id = :id")
    internal abstract fun getReceiptDraftRelationById(id: Long): Flow<ReceiptDraftRelation>

    @Transaction
    @Query(value = "SELECT * FROM receipt_drafts INNER JOIN (SELECT * FROM receipts) receipt ON receipt_uid = receipt_draft_receipt_uid ORDER BY receipt_date ASC")
    internal abstract fun getReceiptRelations(): Flow<List<ReceiptDraftRelation>>

    fun getRelations(): Flow<List<ReceiptDraftRelation>> {
        return getReceiptRelations()
            .map { setReceiptDetailRelations(it) }
    }

    fun getRelationById(id: Long): Flow<ReceiptDraftRelation> {
        return getReceiptDraftRelationById(id)
            .map { setReceiptDetailRelations(it) }
    }

    private fun setReceiptDetailRelations(
        relations: List<ReceiptDraftRelation>
    ): List<ReceiptDraftRelation> {
        return relations.map { setReceiptDetailRelations(it) }
    }

    private fun setReceiptDetailRelations(relation: ReceiptDraftRelation): ReceiptDraftRelation {
        val relations: List<ReceiptDetailRelation> =
            receiptDetailRelationDao.getRelationsByReceiptUid(relation.receiptEntity.uid)
        return relation.copy(receiptDetailRelations = relations)
    }
}