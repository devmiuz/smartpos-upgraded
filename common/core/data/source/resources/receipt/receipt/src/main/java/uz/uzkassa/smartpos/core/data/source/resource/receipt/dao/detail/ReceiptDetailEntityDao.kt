package uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailEntity

@Dao
abstract class ReceiptDetailEntityDao : BaseDao.Impl<ReceiptDetailEntity>() {

    @Query(value = "DELETE FROM receipt_details WHERE receipt_detail_receipt_uid = :receiptUid")
    abstract fun deleteByReceiptUid(receiptUid: String)
}