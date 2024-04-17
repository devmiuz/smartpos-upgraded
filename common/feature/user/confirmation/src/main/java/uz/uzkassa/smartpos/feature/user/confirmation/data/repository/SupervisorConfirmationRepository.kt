package uz.uzkassa.smartpos.feature.user.confirmation.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.params.SupervisorConfirmationParams

internal interface SupervisorConfirmationRepository {

    fun getUserRoleType(): UserRole.Type

    fun confirmUser(params: SupervisorConfirmationParams): Flow<Unit>
}