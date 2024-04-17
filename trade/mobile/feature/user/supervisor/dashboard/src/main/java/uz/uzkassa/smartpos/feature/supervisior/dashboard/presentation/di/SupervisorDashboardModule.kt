package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.data.source.resource.branch.BranchStore
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.CompanyStore
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.ReceiptTemplateStore
import uz.uzkassa.smartpos.core.data.source.resource.user.UserStore
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.user.UserRepositoryImpl
import uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository

@Module(includes = [SupervisorDashboardModule.Binders::class, SupervisorDashboardModule.Providers::class])
internal object SupervisorDashboardModule {

    @Module
    interface Binders {

        @Binds
        @SupervisorDashboardScope
        fun bindUserProcessDirectionUserRepository(impl: UserRepositoryImpl): UserRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @SupervisorDashboardScope
        fun provideDrawerStateDelegate(): DrawerStateDelegate =
            DrawerStateDelegate()

        @JvmStatic
        @Provides
        @SupervisorDashboardScope
        fun provideSyncRepository(
            branchStore: BranchStore,
            categoryEntityDao: CategoryEntityDao,
            categoryRestService: CategoryRestService,
            categorySyncTimePreference: CategorySyncTimePreference,
            companyStore: CompanyStore,
            productEntityDao: ProductEntityDao,
            productRestService: ProductRestService,
            productSyncTimePreference: ProductSyncTimePreference,
            receiptTemplateStore: ReceiptTemplateStore,
            userStore: UserStore
        ) : SyncRepository =
            SyncRepository.instantiate(
                branchStore = branchStore,
                categoryEntityDao = categoryEntityDao,
                categoryRestService = categoryRestService,
                categorySyncTimePreference = categorySyncTimePreference,
                companyStore = companyStore,
                productEntityDao = productEntityDao,
                productRestService = productRestService,
                productSyncTimePreference = productSyncTimePreference,
                receiptTemplateStore = receiptTemplateStore,
                userStore = userStore
            )
    }
}