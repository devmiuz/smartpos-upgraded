package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransaction
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransactionEntity
import java.math.BigDecimal

@Dao
abstract class CashTransactionEntityDao : BaseDao.Impl<CashTransactionEntity>() {

    @Query(value = "SELECT * FROM cash_transactions")
    abstract fun getEntities(): Flow<List<CashTransactionEntity>>

    @Query(value = "SELECT * FROM cash_transactions WHERE cash_transaction_receipt_uid = :receiptUid")
    abstract fun getEntityByReceiptUid(receiptUid: String): Flow<CashTransactionEntity>

    @Query(value = "SELECT * FROM cash_transactions WHERE cash_transaction_shift_id = (SELECT cash_transaction_shift_id FROM cash_transactions ORDER BY ROWID DESC LIMIT 1 )")
    abstract fun getEntitiesForLastShift(): Flow<List<CashTransactionEntity>>

    @Query(value = "SELECT * FROM cash_transactions WHERE cash_transaction_shift_id = :shiftId")
    abstract fun getEntitiesByShiftId(shiftId: Long): Flow<List<CashTransactionEntity>>

    @Query(value = "SELECT * FROM cash_transactions WHERE cash_transaction_is_sync = :isSync")
    abstract fun getEntitiesBySync(isSync: Boolean): Flow<List<CashTransactionEntity>>

    @Query(value = "SELECT * FROM cash_transactions WHERE cash_transaction_is_sync = :isSync AND cash_transaction_operation IN (:operations)")
    abstract fun getEntitiesBySyncWithOperations(
        isSync: Boolean,
        operations: List<String>
    ): Flow<List<CashTransactionEntity>>

    @Query(value = "SELECT * FROM cash_transactions WHERE cash_transaction_is_sync = :isSync AND cash_transaction_operation NOT IN (:operations)")
    abstract fun getEntitiesBySyncWithoutOperations(
        isSync: Boolean,
        operations: List<String>
    ): Flow<List<CashTransactionEntity>>

    @Query(value = "SELECT * FROM cash_transactions WHERE cash_transaction_operation = :operation")
    abstract fun getEntitiesByOperation(operation: String): Flow<List<CashTransactionEntity>>

    @Query(value = "SELECT cash_transaction_total_amount FROM cash_transactions ORDER BY ROWID DESC LIMIT 1")
    abstract fun getCashTotalAmount(): BigDecimal?

    @Query(value = " SELECT SUM(cash_transaction_total_cash) as amount FROM cash_transactions WHERE cash_transaction_operation = :operation ")
    abstract fun getCashTotalAmountByCashOperation(operation: String): BigDecimal?

    @Query(value = "UPDATE cash_transactions SET cash_transaction_is_sync = 1 WHERE cash_transaction_receipt_uid IN (:uniqueIds)")
    abstract fun updateSyncState(uniqueIds: List<String>)

    @Deprecated(
        message = "",
        replaceWith = ReplaceWith("throw UnsupportedOperationException()"),
        level = DeprecationLevel.HIDDEN
    )
    override fun upsert(collection: Collection<CashTransactionEntity>) =
        throw UnsupportedOperationException()

    @Deprecated(
        message = "",
        replaceWith = ReplaceWith("throw UnsupportedOperationException()"),
        level = DeprecationLevel.HIDDEN
    )
    override fun upsert(value: CashTransactionEntity) =
        throw UnsupportedOperationException()

    @FlowPreview
    fun upsert(cashTransaction: CashTransaction, shiftId: Long, shiftNumber: Long) {
        val totalAmount: BigDecimal = getCashTotalAmount() ?: BigDecimal.ZERO

        val cashAmount: BigDecimal = when (cashTransaction.operation) {
            CashOperation.INCOME,
            CashOperation.PAID,
//            CashOperation.ADVANCE,
//            CashOperation.CREDIT,
            CashOperation.RETURN_FLOW -> {
                totalAmount + cashTransaction.totalCash
            }

            CashOperation.FLOW,
            CashOperation.INCASSATION,
            CashOperation.RETURNED,
            CashOperation.WITHDRAW,
            CashOperation.UNKNOWN -> {
                if (totalAmount < cashTransaction.totalCash) totalAmount
                else totalAmount - cashTransaction.totalCash
            }
        }
        super.upsert(cashTransaction.map(cashAmount, shiftId, shiftNumber))
    }
}