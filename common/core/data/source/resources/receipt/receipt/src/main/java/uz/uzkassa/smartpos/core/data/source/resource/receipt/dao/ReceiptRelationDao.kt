package uz.uzkassa.smartpos.core.data.source.resource.receipt.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus

@Dao
abstract class ReceiptRelationDao(database: SupportRoomDatabase) : BaseDao.Impl<ReceiptEntity>() {
    private val receiptDetailRelationDao = database.getDao<ReceiptDetailRelationDao>()

    @Transaction
    @Query(value = "SELECT * FROM receipts WHERE receipt_is_synced = :isSynced LIMIT 100")
    internal abstract fun getReceiptRelationsBySync(isSynced: Boolean): List<ReceiptRelation>

    @Transaction
    @Query(value = "SELECT * FROM receipts ORDER BY receipt_date DESC")
    internal abstract fun getReceiptRelations(): Flow<List<ReceiptRelation>>

    @Transaction
    @Query(value = "SELECT * FROM receipts WHERE receipt_uid = :receiptUid")
    internal abstract fun getReceiptRelationByReceiptUid(receiptUid: String): ReceiptRelation

    @Transaction
    @Query(value = "SELECT * FROM receipts WHERE receipt_status_receipt_status = :status")
    internal abstract fun getReceiptRelationsByStatus(status: String): Flow<List<ReceiptRelation>>

    @Transaction
    @Query(value = "SELECT * FROM receipts WHERE receipt_status_receipt_status = \"CREDIT\" AND receipt_origin_uid is not null or '' ORDER BY receipt_date DESC")
    internal abstract fun getCreditReceiptRelations(): Flow<List<ReceiptRelation>>

    @Transaction
    @Query(value = "SELECT * FROM receipts WHERE receipt_status_receipt_status = \"ADVANCE\" AND receipt_origin_uid is not null or '' ORDER BY receipt_date DESC")
    internal abstract fun getAdvanceReceiptRelations(): Flow<List<ReceiptRelation>>

    @Transaction
    @RawQuery
    internal abstract fun getReceiptRelationsByFilter(
        query: SupportSQLiteQuery
    ): Flow<List<ReceiptRelation>>


    @Transaction
    @Query(value = "SELECT * FROM receipts WHERE receipt_fiscal_url = :url")
    internal abstract fun getReceiptRelationByReceiptFiscalUrl(url: String): ReceiptRelation

    @WorkerThread
    fun getRelationsBySync(isSynced: Boolean): List<ReceiptRelation> {
        return getReceiptRelationsBySync(isSynced)
            .map { setReceiptDetailRelations(it) }
    }

    fun getRelationsByStatus(receiptStatus: ReceiptStatus): Flow<List<ReceiptRelation>> {
        return getReceiptRelationsByStatus(receiptStatus.name)
            .map { setReceiptDetailRelations(it) }
    }

    fun getCreditReceiptsRelations(): Flow<List<ReceiptRelation>> {
        return getCreditReceiptRelations()
            .map { setReceiptDetailRelations(it) }
    }

    fun getAdvanceReceiptsRelations(): Flow<List<ReceiptRelation>> {
        return getAdvanceReceiptRelations()
            .map { setReceiptDetailRelations(it) }
    }

    fun getRelationByFilter(query: SupportSQLiteQuery): Flow<List<ReceiptRelation>> {
        return getReceiptRelationsByFilter(query = query)
            .map { setReceiptDetailRelations(it) }
    }

    fun getCreditAdvanceReceiptsRelations(): Flow<List<ReceiptRelation>> {
        return getReceiptRelations()
            .map { setReceiptDetailRelations(it) }
    }

    fun getRelationByFiscalUrl(url: String): ReceiptRelation {
        return setReceiptDetailRelations(getReceiptRelationByReceiptFiscalUrl(url))
    }

    fun getRelationByUid(uid: String): ReceiptRelation {
        return setReceiptDetailRelations(getReceiptRelationByReceiptUid(uid))
    }

    private fun setReceiptDetailRelations(
        relations: List<ReceiptRelation>
    ): List<ReceiptRelation> {
        return relations.map { setReceiptDetailRelations(it) }
    }

    private fun setReceiptDetailRelations(relation: ReceiptRelation): ReceiptRelation {
        val relations: List<ReceiptDetailRelation> =
            receiptDetailRelationDao.getRelationsByReceiptUid(relation.receiptEntity.uid)
        return relation.copy(receiptDetailRelations = relations)
    }
}