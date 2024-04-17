package uz.uzkassa.smartpos.trade.data.database

import androidx.room.Database
import androidx.room.TypeConverters
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.dao.SalesDynamicsEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsEntity
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryEntity
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyEntity
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.CustomerEntity
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarkingEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductPaginationRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao.ProductPackageTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.transaction.CashTransactionEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment.ReceiptPaymentEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraftEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptAmountEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.PostponeReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmountEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetailEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPaymentEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateEntity
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionEntity
import uz.uzkassa.smartpos.core.data.source.resource.shift.dao.ShiftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.model.ShiftEntity
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportEntity
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleEntity
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.dao.ReceiptTotalDao
import uz.uzkassa.smartpos.trade.data.database.impl.typeconverter.RoomTypeConverters
import kotlin.reflect.KClass

@TypeConverters(RoomTypeConverters::class)
@Database(
    entities = [
        ActivityTypeEntity::class,
        BranchEntity::class,
        CardEntity::class,
        CardTypeEntity::class,
        CashTransactionEntity::class,
        CategoryEntity::class,
        CityEntity::class,
        CompanyBusinessTypeEntity::class,
        CompanyEntity::class,
        CustomerEntity::class,
        PostponeReceiptAmountEntity::class,
        PostponeReceiptDetailEntity::class,
        PostponeReceiptEntity::class,
        ProductMarkingEntity::class,
        ProductEntity::class,
        ProductPackageTypeEntity::class,
        ProductUnitEntity::class,
        RegionEntity::class,
        ReceiptEntity::class,
        ReceiptDetailEntity::class,
        ReceiptDraftEntity::class,
        ReceiptPaymentEntity::class,
        ReceiptTemplateEntity::class,
        SalesDynamicsEntity::class,
        ShiftEntity::class,
        ShiftReportEntity::class,
        UserEntity::class,
        UserRoleEntity::class,
        UnitEntity::class
    ],
    version = DatabaseProvider.DATABASE_VERSION,
    exportSchema = false
)
internal abstract class DatabaseProviderImpl : SupportRoomDatabase(), DatabaseProvider {


    override fun clearAll() {
        this.clearAllTables()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseDao> getDao(kClass: KClass<*>): T {
        val baseDao: BaseDao = when (kClass) {
            ActivityTypeEntityDao::class -> activityTypeEntityDao
            BranchEntityDao::class -> branchEntityDao
            BranchRelationDao::class -> branchRelationDao
            CardEntityDao::class -> cardEntityDao
            CardTypeEntityDao::class -> cardTypeEntityDao
            CashTransactionEntityDao::class -> cashTransactionEntityDao
            CategoryEntityDao::class -> categoryEntityDao
            CategoryRelationDao::class -> categoryRelationDao
            CityEntityDao::class -> cityEntityDao
            CompanyEntityDao::class -> companyEntityDao
            CompanyRelationDao::class -> companyRelationDao
            CompanyBusinessTypeEntityDao::class -> companyBusinessTypeEntityDao
            PostponeReceiptAmountEntityDao::class -> postponeReceiptAmountEntityDao
            PostponeReceiptEntityDao::class -> postponeReceiptEntityDao
            PostponeReceiptDetailEntityDao::class -> postponeReceiptDetailEntityDao
            ProductEntityDao::class -> productEntityDao
            ProductPackageTypeEntityDao::class -> productPackageTypeEntityDao
            ProductPaginationRelationDao::class -> productPaginationRelationDao
            ProductRelationDao::class -> productRelationDao
            ProductMarkingDao::class -> productMarkingDao
            ProductUnitEntityDao::class -> productUnitEntityDao
            ProductUnitRelationDao::class -> productUnitRelationDao
            RegionEntityDao::class -> regionEntityDao
            ReceiptDetailEntityDao::class -> receiptDetailEntityDao
            ReceiptDetailRelationDao::class -> receiptDetailRelationDao
            ReceiptDraftEntityDao::class -> receiptDraftEntityDao
            ReceiptDraftRelationDao::class -> receiptDraftRelationDao
            ReceiptEntityDao::class -> receiptEntityDao
            ReceiptPaymentEntityDao::class -> receiptPaymentEntityDao
            ReceiptRelationDao::class -> receiptRelationDao
            ReceiptTemplateEntityDao::class -> receiptTemplateEntityDao
            ReceiptTotalDao::class -> receiptTotalDao
            SalesDynamicsEntityDao::class -> salesDynamicsEntityDao
            ShiftEntityDao::class -> shiftEntityDao
            ShiftReportEntityDao::class -> shiftReportEntityDao
            UnitEntityDao::class -> unitEntityDao
            UserEntityDao::class -> userEntityDao
            UserRelationDao::class -> userRelationDao
            UserRoleEntityDao::class -> userRoleEntityDao
            else -> error("Unable to get instance of ${kClass.simpleName}")
        }

        return baseDao as T
    }
}