package uz.uzkassa.smartpos.feature.user.confirmation.data.repository.params

import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin.params.BranchAdminConfirmationParams
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner.params.OwnerConfirmationParams

internal data class SupervisorConfirmationParams(val password: String) {

    fun asBranchAdminConfirmation(): BranchAdminConfirmationParams =
        BranchAdminConfirmationParams(password)

    fun asOwnerConfirmationParams(): OwnerConfirmationParams =
        OwnerConfirmationParams(password)
}