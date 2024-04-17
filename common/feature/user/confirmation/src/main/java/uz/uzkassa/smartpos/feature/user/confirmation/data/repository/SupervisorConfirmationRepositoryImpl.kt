package uz.uzkassa.smartpos.feature.user.confirmation.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin.BranchAdminConfirmationRepository
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner.OwnerConfirmationRepository
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.params.SupervisorConfirmationParams
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureArgs
import javax.inject.Inject

internal class SupervisorConfirmationRepositoryImpl @Inject constructor(
    private val branchAdminConfirmationRepository: BranchAdminConfirmationRepository,
    private val ownerConfirmationRepository: OwnerConfirmationRepository,
    private val userConfirmationFeatureArgs: UserConfirmationFeatureArgs
) : SupervisorConfirmationRepository {

    override fun getUserRoleType(): UserRole.Type =
        userConfirmationFeatureArgs.userRoleType

    override fun confirmUser(params: SupervisorConfirmationParams): Flow<Unit> {
        return when (userConfirmationFeatureArgs.userRoleType) {
            UserRole.Type.BRANCH_ADMIN ->
                branchAdminConfirmationRepository
                    .confirmBranchAdmin(params.asBranchAdminConfirmation())
            UserRole.Type.OWNER ->
                ownerConfirmationRepository
                    .confirmOwner(params.asOwnerConfirmationParams())
            else -> throw UnsupportedOperationException("Unable to define user")
        }
    }
}