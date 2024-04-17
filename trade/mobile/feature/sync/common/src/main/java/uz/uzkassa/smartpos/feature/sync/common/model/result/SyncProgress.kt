package uz.uzkassa.smartpos.feature.sync.common.model.result

import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

sealed class SyncProgress {

    sealed class CategoriesProgress : ProgressState {
        internal object Init : CategoriesProgress()
        object Start : CategoriesProgress(), ProgressState.Start
        object Success : CategoriesProgress(), ProgressState.Success
        data class Failure(
            override val throwable: Throwable
        ) : CategoriesProgress(), ProgressState.Failure
    }

    sealed class CompanyProgress : ProgressState {
        internal object Init : CompanyProgress()
        object Start : CompanyProgress(), ProgressState.Start
        data class Success(
            val company: Company
        ) : CompanyProgress(), ProgressState.Success
        data class Failure(
            override val throwable: Throwable
        ) : CompanyProgress(), ProgressState.Failure
    }

    sealed class ReceiptTemplateProgress : ProgressState {
        internal object Init : ReceiptTemplateProgress()
        object Start : ReceiptTemplateProgress(), ProgressState.Start
        data class Success(
            val receiptTemplate: ReceiptTemplate?
        ) : ReceiptTemplateProgress(), ProgressState.Success
        data class Failure(
            override val throwable: Throwable
        ) : ReceiptTemplateProgress(), ProgressState.Failure
    }

    sealed class BranchProgress : ProgressState {
        internal object Init : BranchProgress()
        object Start : BranchProgress(), ProgressState.Start
        data class Success(
            val branch: Branch
        ) : BranchProgress(), ProgressState.Success
        data class Failure(
            override val throwable: Throwable
        ) : BranchProgress(), ProgressState.Failure
    }

    sealed class UsersProgress : ProgressState {
        internal object Init : UsersProgress()
        object Start : UsersProgress(), ProgressState.Start
        data class Success(
            val users: List<User>
        ) : UsersProgress(), ProgressState.Success
        data class Failure(
            override val throwable: Throwable
        ) : UsersProgress(), ProgressState.Failure
    }

    sealed class ProductsProgress : ProgressState {
        internal object Init : ProductsProgress()
        object Start : ProductsProgress(), ProgressState.Start
        data class Paging(
            val page: Int,
            val totalPages: Int
        ) : ProductsProgress()
        object Success : ProductsProgress(), ProgressState.Success
        data class Failure(
            val page: Int,
            override val throwable: Throwable
        ) : ProductsProgress(), ProgressState.Failure
    }
}