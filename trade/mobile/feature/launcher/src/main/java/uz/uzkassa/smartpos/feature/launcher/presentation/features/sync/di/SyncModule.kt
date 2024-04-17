package uz.uzkassa.smartpos.feature.launcher.presentation.features.sync.di

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
import uz.uzkassa.smartpos.feature.launcher.data.repository.sync.SyncRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.sync.SyncRepositoryImpl

@Module(includes = [SyncModule.Binders::class, SyncModule.Providers::class])
internal object SyncModule {

    @Module
    interface Binders {

        @Binds
        @SyncScope
        fun bindSyncRepository(
            impl: SyncRepositoryImpl
        ): SyncRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @SyncScope
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
        ) : uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository =
            uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository.instantiate(
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