package uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner.params.OwnerConfirmationParams

internal interface OwnerConfirmationRepository {

    fun confirmOwner(params: OwnerConfirmationParams): Flow<Unit>
}