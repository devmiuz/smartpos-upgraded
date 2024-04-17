package uz.uzkassa.smartpos.feature.supervisior.dashboard.data.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.model.ReceiptTotalDetails

@Dao
interface ReceiptTotalDao : BaseDao {

    @Query(value = "SELECT AVG(receipt_total_cost) AS averageReceiptCost,  receipt_date AS date, SUM(receipt_total_discount) AS discount, SUM(receipt_total_vat) AS vat, SUM(receipt_total_card) AS cardTotal, SUM(receipt_total_cash) AS cashTotal, COUNT(receipt_date) AS count, SUM(receipt_total_card + receipt_total_cash) total FROM receipts WHERE receipt_is_synced = 0 AND receipt_status_receipt_status = :status GROUP BY DATE(receipt_date) ")
    fun getReceiptTotalDetailsByStatus(status: String): List<ReceiptTotalDetails>
}