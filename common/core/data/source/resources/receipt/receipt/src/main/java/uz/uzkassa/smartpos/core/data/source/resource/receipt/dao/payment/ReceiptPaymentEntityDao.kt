package uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentEntity

@Dao
abstract class ReceiptPaymentEntityDao : BaseDao.Impl<ReceiptPaymentEntity>() {

    @Query(value = "DELETE FROM receipt_payments WHERE receipt_payment_receipt_uid = :receiptUid")
    abstract fun deleteByReceiptUid(receiptUid: String)
}