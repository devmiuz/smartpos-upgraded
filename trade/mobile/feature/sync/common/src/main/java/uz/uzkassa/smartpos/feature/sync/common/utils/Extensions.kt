@file:Suppress("EXPERIMENTAL_API_USAGE")

package uz.uzkassa.smartpos.feature.sync.common.utils

import kotlinx.coroutines.flow.MutableStateFlow
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.*
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncResult

internal fun MutableStateFlow<SyncResult>.setBranch(progress: BranchProgress) {
    value = value.copy(branch = progress)
}

internal fun MutableStateFlow<SyncResult>.setCategories(progress: CategoriesProgress) {
    value = value.copy(categories = progress)
}

fun MutableStateFlow<SyncResult>.setCompany(progress: CompanyProgress) {
    value = value.copy(company = progress)
}

internal fun MutableStateFlow<SyncResult>.setProducts(progress: ProductsProgress) {
    value = value.copy(products = progress)
}

internal fun MutableStateFlow<SyncResult>.setReceiptTemplate(progress: ReceiptTemplateProgress) {
    value = value.copy(receiptTemplate = progress)
}

internal fun MutableStateFlow<SyncResult>.setUsers(progress: UsersProgress) {
    value = value.copy(users = progress)
}