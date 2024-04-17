package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateEntity

@Dao
abstract class ReceiptTemplateEntityDao : BaseDao.Impl<ReceiptTemplateEntity>() {

    @Query(value = "SELECT * FROM receipt_templates")
    abstract fun getEntity(): ReceiptTemplateEntity?

    @Query(value = "DELETE FROM receipt_templates WHERE receipt_template_company_id = :companyId")
    internal abstract fun deleteByCompanyId(companyId: Long)

    @Query(value = "DELETE FROM receipt_templates")
    abstract fun deleteAll()

    fun save(response: ReceiptTemplateResponse) {
        deleteByCompanyId(response.companyId)
        insertOrUpdate(response.mapToEntity())
    }
}