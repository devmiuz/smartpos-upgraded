package uz.uzkassa.smartpos.feature.sync.common.model.result

import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.*

data class SyncResult internal constructor(
    val company: CompanyProgress = CompanyProgress.Init,
    val receiptTemplate: ReceiptTemplateProgress = ReceiptTemplateProgress.Init,
    val branch: BranchProgress = BranchProgress.Init,
    val users: UsersProgress = UsersProgress.Init,
    val categories: CategoriesProgress = CategoriesProgress.Init,
    val products: ProductsProgress = ProductsProgress.Init
) {

    val isFinished: Boolean
        get() = company.isFinished && receiptTemplate.isFinished &&
                branch.isFinished && users.isFinished &&
                categories.isFinished && products.isFinished
}