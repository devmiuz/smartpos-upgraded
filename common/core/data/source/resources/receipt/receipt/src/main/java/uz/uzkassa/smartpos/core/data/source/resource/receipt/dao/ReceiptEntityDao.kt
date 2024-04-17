package uz.uzkassa.smartpos.core.data.source.resource.receipt.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment.ReceiptPaymentEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToEntitiesWithBaseStatus
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.payment.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus

@Dao
abstract class ReceiptEntityDao(
    database: SupportRoomDatabase
) : BaseDao.Impl<ReceiptEntity>() {
    private val receiptDetailEntityDao = database.getDao<ReceiptDetailEntityDao>()
    private val receiptPaymentEntityDao = database.getDao<ReceiptPaymentEntityDao>()

    @Query(value = "SELECT COUNT(receipt_uid) FROM receipts WHERE receipt_uid = :uid")
    abstract fun isExistsByUid(uid: String): Boolean

    @Query(value = "DELETE FROM receipts WHERE receipt_uid = :receiptUid")
    abstract fun internalDeleteByReceiptUid(receiptUid: String)

    @Query(value = "UPDATE receipts SET receipt_is_synced = 1 WHERE receipt_uid IN (:uniqueIds)")
    abstract fun updateSyncState(uniqueIds: List<String>)

    @Query(value = "SELECT * FROM receipts WHERE receipt_uid = :uid")
    abstract fun getByUid(uid: String): ReceiptEntity?

    fun deleteByReceiptUid(receiptUid: String) {
        receiptDetailEntityDao.deleteByReceiptUid(receiptUid)
        receiptPaymentEntityDao.deleteByReceiptUid(receiptUid)
        internalDeleteByReceiptUid(receiptUid)
    }

    fun isDraftReceipt(uid: String): Boolean {
        val receipt = getByUid(uid)
        return receipt?.status.equals("DRAFT")
    }

    fun save(receipt: Receipt) {
        with(receipt) {
            deleteByReceiptUid(receipt.uid)
            receiptDetailEntityDao.insertOrUpdate(receiptDetails.mapToEntities(uid))
            receiptPaymentEntityDao.insertOrUpdate(receiptPayments.mapToEntity(uid))
            insertOrUpdate(mapToEntity(isSynced = false))
        }
    }

    fun save(response: ReceiptResponse) {
        with(response) {
            deleteByReceiptUid(response.uid)
            receiptDetailEntityDao.insertOrUpdate(receiptDetails.mapToEntities(uid))
            receiptPaymentEntityDao.insertOrUpdate(receiptPayments.responses.mapToEntities(uid))
            return@with insertOrUpdate(mapToEntity())
        }
    }

    fun save(responses: List<ReceiptResponse>) {
        receiptDetailEntityDao
            .insertOrUpdate(responses.flatMap { it.receiptDetails.mapToEntities(it.uid) })
        receiptPaymentEntityDao
            .insertOrUpdate(responses.flatMap { it.receiptPayments.responses.mapToEntities(it.uid) })
        insertOrUpdate(responses.mapToEntities())
    }

    fun saveWithBaseStatus(responses: List<ReceiptResponse>, baseStatus: ReceiptStatus) {
        receiptDetailEntityDao
            .insertOrUpdate(responses.flatMap { it.receiptDetails.mapToEntities(it.uid) })
        receiptPaymentEntityDao
            .insertOrUpdate(responses.flatMap { it.receiptPayments.responses.mapToEntities(it.uid) })
        insertOrUpdate(responses.mapToEntitiesWithBaseStatus(baseStatus))
    }
}