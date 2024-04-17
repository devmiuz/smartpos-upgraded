package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.PostponeReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.PostponeReceiptRelation

@Dao
abstract class PostponeReceiptEntityDao : BaseDao.Impl<PostponeReceiptEntity>() {

    @Query(value = "DELETE FROM postpone_receipts WHERE postpone_receipt_id = :id")
    abstract fun deleteById(id: Long)

    @Transaction
    @Query(value = "SELECT * FROM postpone_receipts WHERE postpone_receipt_id = :id")
    abstract fun getRelationById(id: Long): Flow<PostponeReceiptRelation>

    @Transaction
    @Query(value = "SELECT * FROM postpone_receipts")
    abstract fun getRelations(): Flow<List<PostponeReceiptRelation>>

    @Query(value = "SELECT COUNT(postpone_receipt_uid) FROM postpone_receipts WHERE postpone_receipt_uid = :uid")
    abstract fun isExistsByUid(uid: String): Boolean
}