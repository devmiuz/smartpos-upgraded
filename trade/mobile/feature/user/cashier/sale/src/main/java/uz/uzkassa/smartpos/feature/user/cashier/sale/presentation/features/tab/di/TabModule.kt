package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.shift.ShiftReportRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.shift.ShiftReportRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.sync.SyncRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.sync.SyncRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.UserRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.session.UserSessionRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.session.UserSessionRepositoryImpl

@Module(includes = [TabModule.Binders::class, TabModule.Providers::class])
internal object TabModule {

    @Module
    interface Binders {

        @Binds
        @TabScope
        fun bindShiftReportRepository(
            shiftReportRepositoryImpl: ShiftReportRepositoryImpl
        ): ShiftReportRepository

        @Binds
        @TabScope
        fun bindUserRepository(
            userRepositoryImpl: UserRepositoryImpl
        ): UserRepository

        @Binds
        @TabScope
        fun bindUserSessionRepository(
            userSessionRepositoryImpl: UserSessionRepositoryImpl
        ): UserSessionRepository

        @Binds
        fun syncRepository(impl: SyncRepositoryImpl) : SyncRepository
    }

    @Module
    object Providers {

//        @JvmStatic
//        @Provides
//        @TabScope
//        fun provideSyncRepository(
//            branchStore: BranchStore,
//            categoryEntityDao: CategoryEntityDao,
//            categoryRestService: CategoryRestService,
//            categorySyncTimePreference: CategorySyncTimePreference,
//            companyStore: CompanyStore,
//            productEntityDao: ProductEntityDao,
//            productRestService: ProductRestService,
//            productSyncTimePreference: ProductSyncTimePreference,
//            receiptTemplateStore: ReceiptTemplateStore,
//            userStore: UserStore
//        ) : SyncRepository =
//            SyncRepository.instantiate(
//                branchStore = branchStore,
//                categoryEntityDao = categoryEntityDao,
//                categoryRestService = categoryRestService,
//                categorySyncTimePreference = categorySyncTimePreference,
//                companyStore = companyStore,
//                productEntityDao = productEntityDao,
//                productRestService = productRestService,
//                productSyncTimePreference = productSyncTimePreference,
//                receiptTemplateStore = receiptTemplateStore,
//                userStore = userStore
//            )
    }
}