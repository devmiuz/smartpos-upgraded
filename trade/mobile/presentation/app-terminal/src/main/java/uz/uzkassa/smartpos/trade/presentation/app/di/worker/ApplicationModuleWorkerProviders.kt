package uz.uzkassa.smartpos.trade.presentation.app.di.worker

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
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
import uz.uzkassa.smartpos.feature.receipt.sync.worker.ReceiptSyncWorker
import uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository
import uz.uzkassa.smartpos.feature.sync.worker.SyncWorker
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.worker.CashOperationSyncWorker
import uz.uzkassa.smartpos.trade.presentation.app.di.worker.ApplicationModuleWorkerProviders.Binders
import uz.uzkassa.smartpos.trade.presentation.app.di.worker.ApplicationModuleWorkerProviders.Providers
import uz.uzkassa.smartpos.trade.presentation.app.worker.provider.CashOperationSyncWorkerProvider
import uz.uzkassa.smartpos.trade.presentation.app.worker.provider.ReceiptSyncWorkerProvider
import uz.uzkassa.smartpos.trade.presentation.app.worker.provider.SyncWorkerProvider
import uz.uzkassa.smartpos.trade.presentation.support.dagger2.worker.WorkerKey
import uz.uzkassa.smartpos.trade.presentation.support.dagger2.worker.WorkerProvider

@Module(includes = [Binders::class, Providers::class])
object ApplicationModuleWorkerProviders {

    @Module
    interface Binders {

        @Binds
        @IntoMap
        @WorkerKey(CashOperationSyncWorker::class)
        fun bindCashOperationSyncWorkerProvider(provider: CashOperationSyncWorkerProvider): WorkerProvider

        @Binds
        @IntoMap
        @WorkerKey(ReceiptSyncWorker::class)
        fun bindReceiptWorkerProvider(provider: ReceiptSyncWorkerProvider): WorkerProvider

        @Binds
        @IntoMap
        @WorkerKey(SyncWorker::class)
        fun bindSyncWorkerProvider(provider: SyncWorkerProvider): WorkerProvider
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
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
        ): SyncRepository =
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