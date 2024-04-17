package uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.modules

import android.content.Context
import dagger.Module
import dagger.Provides
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
import uz.uzkassa.smartpos.trade.companion.data.database.DatabaseProvider
import javax.inject.Singleton

@Module(includes = [ApplicationDataModuleDatabase.Providers::class])
object ApplicationDataModuleDatabase {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideDatabaseProvider(
            context: Context
        ): DatabaseProvider =
            DatabaseProvider.instantiate(context)

        @JvmStatic
        @Provides
        @Singleton
        fun provideActivityTypeEntityDao(
            databaseProvider: DatabaseProvider
        ): ActivityTypeEntityDao =
            databaseProvider.activityTypeEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideBranchEntityDao(
            databaseProvider: DatabaseProvider
        ): BranchEntityDao =
            databaseProvider.branchEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideCardEntityDao(
            databaseProvider: DatabaseProvider
        ): CardEntityDao =
            databaseProvider.cardEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideCardTypeEntityDao(
            databaseProvider: DatabaseProvider
        ): CardTypeEntityDao =
            databaseProvider.cardTypeEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideCategoryEntityDao(
            databaseProvider: DatabaseProvider
        ): CategoryEntityDao =
            databaseProvider.categoryEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideCityEntityDao(
            databaseProvider: DatabaseProvider
        ): CityEntityDao =
            databaseProvider.cityEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideCompanyEntityDao(
            databaseProvider: DatabaseProvider
        ): CompanyEntityDao =
            databaseProvider.companyEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideCompanyBusinessTypeEntityDao(
            databaseProvider: DatabaseProvider
        ): CompanyBusinessTypeEntityDao =
            databaseProvider.companyBusinessTypeEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun providePostponeReceiptAmountEntityDao(
            databaseProvider: DatabaseProvider
        ): PostponeReceiptAmountEntityDao =
            databaseProvider.postponeReceiptAmountEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun providePostponeReceiptEntityDao(
            databaseProvider: DatabaseProvider
        ): PostponeReceiptEntityDao =
            databaseProvider.postponeReceiptEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun providePostponeReceiptDetailEntityDao(
            databaseProvider: DatabaseProvider
        ): PostponeReceiptDetailEntityDao =
            databaseProvider.postponeReceiptDetailEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideProductEntityDao(
            databaseProvider: DatabaseProvider
        ): ProductEntityDao =
            databaseProvider.productEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideProductPackageTypeEntityDao(
            databaseProvider: DatabaseProvider
        ): ProductPackageTypeEntityDao =
            databaseProvider.productPackageTypeEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideProductUnitEntityDao(
            databaseProvider: DatabaseProvider
        ): ProductUnitEntityDao =
            databaseProvider.productUnitEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideRegionEntityDao(
            databaseProvider: DatabaseProvider
        ): RegionEntityDao =
            databaseProvider.regionEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideReceiptDetailEntityDao(
            databaseProvider: DatabaseProvider
        ): ReceiptDetailEntityDao =
            databaseProvider.receiptDetailEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideReceiptEntityDao(
            databaseProvider: DatabaseProvider
        ): ReceiptEntityDao =
            databaseProvider.receiptEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideReceiptTemplateEntityDao(
            databaseProvider: DatabaseProvider
        ): ReceiptTemplateEntityDao =
            databaseProvider.receiptTemplateEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideShiftReportEntityDao(
            databaseProvider: DatabaseProvider
        ): ShiftReportEntityDao =
            databaseProvider.shiftReportEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideUnitEntityDao(
            databaseProvider: DatabaseProvider
        ): UnitEntityDao =
            databaseProvider.unitEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideUserEntityDao(
            databaseProvider: DatabaseProvider
        ): UserEntityDao =
            databaseProvider.userEntityDao

        @JvmStatic
        @Provides
        @Singleton
        fun provideUserRoleEntityDao(
            databaseProvider: DatabaseProvider
        ): UserRoleEntityDao =
            databaseProvider.userRoleEntityDao
    }
}