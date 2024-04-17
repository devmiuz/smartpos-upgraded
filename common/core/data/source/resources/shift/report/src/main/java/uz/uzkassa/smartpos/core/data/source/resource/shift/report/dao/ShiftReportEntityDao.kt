package uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportEntity
import java.math.BigDecimal
import java.util.*

@Dao
abstract class ShiftReportEntityDao : BaseDao.Impl<ShiftReportEntity>() {

    @Query(value = "SELECT * FROM shift_reports WHERE shift_report_is_synced = 0 ORDER BY shift_report_id DESC LIMIT 1")
    abstract fun getNotSyncedEntity(): ShiftReportEntity?

    @Query("SELECT * FROM shift_reports WHERE  shift_report_finish_date IS NULL ORDER BY shift_report_id DESC LIMIT 1")
    abstract fun getOpenShiftReportEntity(): Flow<ShiftReportEntity?>

    @Query(value = "UPDATE shift_reports SET shift_report_start_date = :startDate, shift_report_finish_date = :finishDate, shift_report_fiscal_shift_number = :fiscalShiftNumber, shift_report_total_refund_vat = :totalRefundVAT, shift_report_total_refund_card = :totalRefundCard, shift_report_total_refund_cash = :totalRefundCash, shift_report_total_refund_count = :totalRefundCount, shift_report_total_sale_vat = :totalSaleVAT, shift_report_total_sale_card = :totalSaleCard, shift_report_total_sale_cash = :totalSaleCash, shift_report_total_sale_count = :totalSaleCount, shift_report_user_id = :userId WHERE shift_report_id = (SELECT shift_report_id FROM shift_reports ORDER BY shift_report_id DESC LIMIT 1)")
    abstract fun updateNotSyncedEntity(
        startDate: Date?,
        finishDate: Date?,
        fiscalShiftNumber: Int,
        totalRefundVAT: BigDecimal,
        totalRefundCard: BigDecimal,
        totalRefundCash: BigDecimal,
        totalRefundCount: Int,
        totalSaleVAT: BigDecimal,
        totalSaleCard: BigDecimal,
        totalSaleCash: BigDecimal,
        totalSaleCount: Int,
        userId: Long
    )

    @Query(value = "UPDATE shift_reports SET shift_report_is_synced = 1 WHERE shift_report_id = (SELECT shift_report_id FROM shift_reports ORDER BY shift_report_id DESC LIMIT 1)")
    abstract fun updateSyncState()
}