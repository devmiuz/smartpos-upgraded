package uz.uzkassa.smartpos.feature.sync.common.data.repository

import kotlinx.coroutines.flow.Flow
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
import uz.uzkassa.smartpos.feature.sync.common.model.request.SyncRequest
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncResult

interface SyncRepository {

    fun request(syncRequest: SyncRequest): Flow<SyncResult>

    fun sync(syncRequest: SyncRequest): Flow<Unit>

    fun getSyncState(syncRequest: SyncRequest): Flow<Unit>

    companion object {
        
        fun instantiate(
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
            SyncRepositoryImpl(
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