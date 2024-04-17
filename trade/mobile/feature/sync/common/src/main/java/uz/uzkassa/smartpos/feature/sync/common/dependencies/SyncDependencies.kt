package uz.uzkassa.smartpos.feature.sync.common.dependencies

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

interface SyncDependencies {
     val branchStore: BranchStore
     val categoryEntityDao: CategoryEntityDao
     val categoryRestService: CategoryRestService
     val categorySyncTimePreference: CategorySyncTimePreference
     val companyStore: CompanyStore
     val productEntityDao: ProductEntityDao
     val productRestService: ProductRestService
     val productSyncTimePreference: ProductSyncTimePreference
     val receiptTemplateStore: ReceiptTemplateStore
     val userStore: UserStore
}