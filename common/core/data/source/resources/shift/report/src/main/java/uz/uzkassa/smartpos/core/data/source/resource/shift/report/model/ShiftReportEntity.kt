package uz.uzkassa.smartpos.core.data.source.resource.shift.report.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "shift_reports")
data class ShiftReportEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shift_report_id")
    val id: Long,

    @ColumnInfo(name = "shift_report_start_date")
    val startDate: Date?,

    @ColumnInfo(name = "shift_report_finish_date")
    val finishDate: Date?,

    @ColumnInfo(name = "shift_report_fiscal_shift_number")
    val fiscalShiftNumber: Int,

    @ColumnInfo(name = "shift_report_total_refund_vat")
    val totalRefundVAT: BigDecimal,

    @ColumnInfo(name = "shift_report_total_refund_card")
    val totalRefundCard: BigDecimal,

    @ColumnInfo(name = "shift_report_total_refund_cash")
    val totalRefundCash: BigDecimal,

    @ColumnInfo(name = "shift_report_total_refund_count")
    val totalRefundCount: Int,

    @ColumnInfo(name = "shift_report_total_sale_vat")
    val totalSaleVAT: BigDecimal,

    @ColumnInfo(name = "shift_report_total_sale_card")
    val totalSaleCard: BigDecimal,

    @ColumnInfo(name = "shift_report_total_sale_cash")
    val totalSaleCash: BigDecimal,

    @ColumnInfo(name = "shift_report_total_sale_count")
    val totalSaleCount: Int,

    @ColumnInfo(name = "shift_report_user_id")
    val userId: Long,

    @ColumnInfo(name = "shift_report_is_synced")
    val isSynced: Boolean
) {

    @Ignore
    constructor(
        startDate: Date?,
        fiscalShiftNumber: Int,
        userId: Long
    ) : this(
        id = 0,
        startDate = startDate,
        finishDate = null,
        fiscalShiftNumber = fiscalShiftNumber,
        totalRefundVAT = BigDecimal.ZERO,
        totalRefundCard = BigDecimal.ZERO,
        totalRefundCash = BigDecimal.ZERO,
        totalRefundCount = 0,
        totalSaleVAT = BigDecimal.ZERO,
        totalSaleCard = BigDecimal.ZERO,
        totalSaleCash = BigDecimal.ZERO,
        totalSaleCount = 0,
        userId = userId,
        isSynced = false
    )
}