package uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin.params.BranchAdminConfirmationParams

internal interface BranchAdminConfirmationRepository {

    fun confirmBranchAdmin(params: BranchAdminConfirmationParams): Flow<Unit>
}