package uz.uzkassa.smartpos.trade.companion.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryEntity
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyEntity
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.CustomerEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.PostponeReceiptEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.amount.PostponeReceiptAmountEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.model.detail.PostponeReceiptDetailEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateEntity
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionEntity
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportEntity
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleEntity
import uz.uzkassa.smartpos.trade.companion.data.database.impl.typeconverter.RoomTypeConverters

@TypeConverters(RoomTypeConverters::class)
@Database(
    entities = [
        ActivityTypeEntity::class,
        BranchEntity::class,
        CardEntity::class,
        CardTypeEntity::class,
        CategoryEntity::class,
        CityEntity::class,
        CompanyBusinessTypeEntity::class,
        CompanyEntity::class,
        CustomerEntity::class,
        PostponeReceiptAmountEntity::class,
        PostponeReceiptDetailEntity::class,
        PostponeReceiptEntity::class,
        ProductEntity::class,
        ProductPackageTypeEntity::class,
        ProductUnitEntity::class,
        RegionEntity::class,
        ReceiptEntity::class,
        ReceiptDetailEntity::class,
        ReceiptTemplateEntity::class,
        ShiftReportEntity::class,
        UserEntity::class,
        UserRoleEntity::class,
        UnitEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class DatabaseProviderImpl : RoomDatabase(), DatabaseProvider