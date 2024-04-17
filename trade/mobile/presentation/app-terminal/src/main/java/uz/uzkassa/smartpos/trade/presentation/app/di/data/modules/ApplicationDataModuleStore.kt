package uz.uzkassa.smartpos.trade.presentation.app.di.data.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.resource.branch.BranchStore
import uz.uzkassa.smartpos.core.data.source.resource.company.CompanyStore
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.ReceiptTemplateStore
import uz.uzkassa.smartpos.core.data.source.resource.unit.UnitStore
import uz.uzkassa.smartpos.core.data.source.resource.user.UserStore
import uz.uzkassa.smartpos.trade.data.store.StoreProvider
import uz.uzkassa.smartpos.trade.data.store.StoreProviderImpl
import uz.uzkassa.smartpos.trade.presentation.app.di.data.modules.ApplicationDataModuleStore.Binders
import uz.uzkassa.smartpos.trade.presentation.app.di.data.modules.ApplicationDataModuleStore.Providers
import javax.inject.Singleton

@Module(includes = [Binders::class, Providers::class])
object ApplicationDataModuleStore {

    @Module
    interface Binders {

        @Binds
        @Singleton
        fun storeProvider(impl: StoreProviderImpl): StoreProvider
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun branchStore(
            provider: StoreProvider
        ): BranchStore =
            provider.branchStore

        @JvmStatic
        @Provides
        @Singleton
        fun companyStore(
            provider: StoreProvider
        ): CompanyStore =
            provider.companyStore

        @JvmStatic
        @Provides
        @Singleton
        fun receiptTemplateStore(
            provider: StoreProvider
        ): ReceiptTemplateStore =
            provider.receiptTemplateStore

        @JvmStatic
        @Provides
        @Singleton
        fun unitStore(
            provider: StoreProvider
        ): UnitStore =
            provider.unitStore

        @JvmStatic
        @Provides
        @Singleton
        fun userStore(
            provider: StoreProvider
        ): UserStore =
            provider.userStore
    }
}