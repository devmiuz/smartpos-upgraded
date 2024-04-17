package uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.cash

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import java.math.BigDecimal
import javax.inject.Inject

class CashOperationsDetailsRepositoryImpl @Inject constructor(
    private val shiftRepoEntityDao: ShiftReportEntityDao,
    private val cashTransactionEntityDao: CashTransactionEntityDao
) : CashOperationsDetailsRepository {

    override fun getAvailableCash(): Flow<BigDecimal> {
        return shiftRepoEntityDao.getOpenShiftReportEntity()
            .filterNotNull()
            .map { it.id }
            .flatMapConcat { shiftId ->
                cashTransactionEntityDao.getEntitiesByShiftId(shiftId)
                    .map { list -> list.map { it.map() } }
                    .flatMapConcat { transactions ->

                        val totalAmount: BigDecimal = if (transactions.isEmpty()) {
                            cashTransactionEntityDao.getCashTotalAmount() ?: BigDecimal.ZERO
                        } else {
                            transactions.lastOrNull()?.totalAmount ?: BigDecimal.ZERO
                        }

                        val totalCash: BigDecimal = transactions.firstOrNull()?.let {
                            when (it.operation) {
                                CashOperation.INCOME,
                                CashOperation.PAID,
                                CashOperation.RETURN_FLOW -> it.totalAmount - it.totalCash

                                CashOperation.RETURNED,
                                CashOperation.FLOW,
                                CashOperation.INCASSATION,
                                CashOperation.WITHDRAW,
                                CashOperation.UNKNOWN -> it.totalAmount + it.totalCash
                            }
                        } ?: totalAmount

                        val availableCash = totalAmount - totalCash

                        flowOf(availableCash)
                    }
            }
    }
}