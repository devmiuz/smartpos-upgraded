package uz.uzkassa.smartpos.trade.companion.data.database

import android.content.Context
import androidx.room.Room
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao.ProductPackageTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptAmountEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao

interface DatabaseProvider {

    val activityTypeEntityDao: ActivityTypeEntityDao

    val branchEntityDao: BranchEntityDao

    val cardEntityDao: CardEntityDao

    val cardTypeEntityDao: CardTypeEntityDao

    val categoryEntityDao: CategoryEntityDao

    val cityEntityDao: CityEntityDao

    val companyEntityDao: CompanyEntityDao

    val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao

    val postponeReceiptAmountEntityDao: PostponeReceiptAmountEntityDao

    val postponeReceiptEntityDao: PostponeReceiptEntityDao

    val postponeReceiptDetailEntityDao: PostponeReceiptDetailEntityDao

    val productEntityDao: ProductEntityDao

    val productPackageTypeEntityDao: ProductPackageTypeEntityDao

    val productUnitEntityDao: ProductUnitEntityDao

    val regionEntityDao: RegionEntityDao

    val receiptDetailEntityDao: ReceiptDetailEntityDao

    val receiptEntityDao: ReceiptEntityDao

    val receiptTemplateEntityDao: ReceiptTemplateEntityDao

    val shiftReportEntityDao: ShiftReportEntityDao

    val unitEntityDao: UnitEntityDao

    val userEntityDao: UserEntityDao

    val userRoleEntityDao: UserRoleEntityDao

    companion object {
        private const val DATABASE_NAME: String = "persistent_app_database"

        fun instantiate(context: Context): DatabaseProvider =
            Room.databaseBuilder(context, DatabaseProviderImpl::class.java, DATABASE_NAME)
                .addMigrations()
                .build()
    }
}