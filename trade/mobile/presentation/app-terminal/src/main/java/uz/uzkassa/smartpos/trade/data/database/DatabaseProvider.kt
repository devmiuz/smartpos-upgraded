package uz.uzkassa.smartpos.trade.data.database

import android.content.Context
import androidx.room.Room
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.dao.SalesDynamicsEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductPaginationRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao.ProductPackageTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment.ReceiptPaymentEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptAmountEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.dao.ShiftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.dao.ReceiptTotalDao
import uz.uzkassa.smartpos.trade.data.database.impl.migration.DatabaseMigration

interface DatabaseProvider {

    val activityTypeEntityDao: ActivityTypeEntityDao

    val branchEntityDao: BranchEntityDao

    val branchRelationDao: BranchRelationDao

    val cardEntityDao: CardEntityDao

    val cardTypeEntityDao: CardTypeEntityDao

    val cashTransactionEntityDao: CashTransactionEntityDao

    val categoryEntityDao: CategoryEntityDao

    val categoryRelationDao: CategoryRelationDao

    val cityEntityDao: CityEntityDao

    val companyEntityDao: CompanyEntityDao

    val companyRelationDao: CompanyRelationDao

    val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao

    val postponeReceiptAmountEntityDao: PostponeReceiptAmountEntityDao

    val postponeReceiptEntityDao: PostponeReceiptEntityDao

    val postponeReceiptDetailEntityDao: PostponeReceiptDetailEntityDao

    val productEntityDao: ProductEntityDao

    val productPackageTypeEntityDao: ProductPackageTypeEntityDao

    val productPaginationRelationDao: ProductPaginationRelationDao

    val productRelationDao: ProductRelationDao

    val productMarkingDao: ProductMarkingDao

    val productUnitEntityDao: ProductUnitEntityDao

    val productUnitRelationDao: ProductUnitRelationDao

    val regionEntityDao: RegionEntityDao

    val receiptDetailEntityDao: ReceiptDetailEntityDao

    val receiptDetailRelationDao: ReceiptDetailRelationDao

    val receiptDraftEntityDao: ReceiptDraftEntityDao

    val receiptDraftRelationDao: ReceiptDraftRelationDao

    val receiptEntityDao: ReceiptEntityDao

    val receiptPaymentEntityDao: ReceiptPaymentEntityDao

    val receiptRelationDao: ReceiptRelationDao

    val receiptTemplateEntityDao: ReceiptTemplateEntityDao

    val receiptTotalDao: ReceiptTotalDao

    val salesDynamicsEntityDao: SalesDynamicsEntityDao

    val shiftEntityDao: ShiftEntityDao

    val shiftReportEntityDao: ShiftReportEntityDao

    val unitEntityDao: UnitEntityDao

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRoleEntityDao: UserRoleEntityDao

    companion object {
        private const val DATABASE_NAME: String = "persistent_app_database"
        const val DATABASE_VERSION: Int = 16

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        fun create(context: Context): DatabaseProvider {
            val path: String = context.filesDir.parent.toString() + "/databases/" + DATABASE_NAME
            return Room.databaseBuilder(context, DatabaseProviderImpl::class.java, path)
                .addMigrations(*DatabaseMigration(path, DATABASE_VERSION).migrations)
                .build()
        }
    }
}