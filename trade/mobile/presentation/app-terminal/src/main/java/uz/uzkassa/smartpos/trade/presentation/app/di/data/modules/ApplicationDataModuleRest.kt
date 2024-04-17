package uz.uzkassa.smartpos.trade.presentation.app.di.data.modules

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.resource.account.service.AccountRestService
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.service.ActivityTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.service.SalesDynamicsRestService
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.card.service.CardRestService
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.city.service.CityRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service.CompanyBusinessTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.service.CompanyVATRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.service.ProductPackageTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service.CashTransactionRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.service.RegionRestService
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.service.ShiftReportRestService
import uz.uzkassa.smartpos.core.data.source.resource.shift.service.ShiftRestService
import uz.uzkassa.smartpos.core.data.source.resource.terminal.service.TerminalRestService
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.apay.data.network.rest.ApayRestService
import uz.uzkassa.smartpos.trade.data.network.rest.RestProvider
import uz.uzkassa.smartpos.trade.data.network.rest.impl.service.ServicesHolder
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.OkHttpInstance
import javax.inject.Singleton

@Module(includes = [ApplicationDataModuleRest.Providers::class])
object ApplicationDataModuleRest {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideRestProvider(
            accountAuthPreference: AccountAuthPreference,
            categorySyncTimePreference: CategorySyncTimePreference,
            productSyncTimePreference: ProductSyncTimePreference,
            okHttpInstance: OkHttpInstance,
            servicesHolder: ServicesHolder,
            userAuthPreference: UserAuthPreference
        ): RestProvider =
            RestProvider.instantiate(
                accountAuthPreference = accountAuthPreference,
                categorySyncTimePreference = categorySyncTimePreference,
                productSyncTimePreference = productSyncTimePreference,
                okHttpInstance = okHttpInstance,
                servicesHolder = servicesHolder,
                userAuthPreference = userAuthPreference
            )

        @JvmStatic
        @Provides
        @Singleton
        fun provideAccountRestService(
            restProvider: RestProvider
        ): AccountRestService =
            restProvider.accountRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideAccountAuthRestService(
            restProvider: RestProvider
        ): AccountAuthRestService =
            restProvider.accountAuthRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideActivityTypeRestService(
            restProvider: RestProvider
        ): ActivityTypeRestService =
            restProvider.activityTypeRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideBranchRestService(
            restProvider: RestProvider
        ): BranchRestService =
            restProvider.branchRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideCardRestService(
            restProvider: RestProvider
        ): CardRestService =
            restProvider.cardRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideCashTransactionRestService(
            restProvider: RestProvider
        ): CashTransactionRestService =
            restProvider.cashTransactionRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideCategoryRestService(
            restProvider: RestProvider
        ): CategoryRestService =
            restProvider.categoryRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideCityRestService(
            restProvider: RestProvider
        ): CityRestService =
            restProvider.cityRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideCompanyRestService(
            restProvider: RestProvider
        ): CompanyRestService =
            restProvider.companyRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideCompanyBusinessTypeRestService(
            restProvider: RestProvider
        ): CompanyBusinessTypeRestService =
            restProvider.companyBusinessTypeRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideCompanyVATRestService(
            restProvider: RestProvider
        ): CompanyVATRestService =
            restProvider.companyVATRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideProductRestService(
            restProvider: RestProvider
        ): ProductRestService =
            restProvider.productRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideProductPackageTypeRestService(
            restProvider: RestProvider
        ): ProductPackageTypeRestService =
            restProvider.productPackageTypeRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideRegionRestService(
            restProvider: RestProvider
        ): RegionRestService =
            restProvider.regionRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideReceiptRestService(
            restProvider: RestProvider
        ): ReceiptRestService =
            restProvider.receiptRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideReceiptTemplateRestService(
            restProvider: RestProvider
        ): ReceiptTemplateRestService =
            restProvider.receiptTemplateRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideSalesDynamicsRest(
            restProvider: RestProvider
        ): SalesDynamicsRestService =
            restProvider.salesDynamicsRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideShiftReportRestService(
            restProvider: RestProvider
        ): ShiftReportRestService =
            restProvider.shiftReportRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideShiftRestService(
            restProvider: RestProvider
        ): ShiftRestService =
            restProvider.shiftRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideTerminalRestService(
            restProvider: RestProvider
        ): TerminalRestService =
            restProvider.terminalRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideUnitRestService(
            restProvider: RestProvider
        ): UnitRestService =
            restProvider.unitRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideUserAuthRestService(
            restProvider: RestProvider
        ): UserAuthRestService =
            restProvider.userAuthRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideUserRestService(
            restProvider: RestProvider
        ): UserRestService =
            restProvider.userRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideUserRoleRestService(
            restProvider: RestProvider
        ): UserRoleRestService =
            restProvider.userRoleRestService

        @JvmStatic
        @Provides
        @Singleton
        fun provideApayRestService(
            restProvider: RestProvider
        ): ApayRestService =
            restProvider.apayRestService
    }
}