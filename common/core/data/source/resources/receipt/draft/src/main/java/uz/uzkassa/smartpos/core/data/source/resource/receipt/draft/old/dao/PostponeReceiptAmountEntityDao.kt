package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmountEntity

@Dao
abstract class PostponeReceiptAmountEntityDao : BaseDao.Impl<PostponeReceiptAmountEntity>() {

    @Query(value = "DELETE FROM postpone_receipt_amounts WHERE postpone_receipt_amount_postpone_receipt_id = :receiptId")
    abstract fun deleteByReceiptId(receiptId: Long)
}