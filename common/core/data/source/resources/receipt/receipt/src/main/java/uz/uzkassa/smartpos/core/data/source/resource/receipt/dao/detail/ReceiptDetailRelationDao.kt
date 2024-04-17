package uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailRelation

@Dao
abstract class ReceiptDetailRelationDao : BaseDao {

    @Query(
        value = """
            SELECT * 
                FROM receipt_details
                JOIN (SELECT company_vat_percent FROM companies) companies 
                LEFT JOIN categories ON category_id = receipt_detail_category_id
                LEFT JOIN units ON unit_id = receipt_detail_unit_id 
                WHERE receipt_detail_receipt_uid = :receiptUid
            """
    )
    abstract fun getRelationsByReceiptUid(receiptUid: String): List<ReceiptDetailRelation>
}