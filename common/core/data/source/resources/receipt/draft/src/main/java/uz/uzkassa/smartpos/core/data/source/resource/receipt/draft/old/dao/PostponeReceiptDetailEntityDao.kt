package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetailEntity

@Dao
abstract class PostponeReceiptDetailEntityDao : BaseDao.Impl<PostponeReceiptDetailEntity>() {

    @Query(value = "DELETE FROM postpone_receipt_details WHERE postpone_receipt_detail_postpone_receipt_id = :receiptId")
    abstract fun deleteByReceiptId(receiptId: Long)
}