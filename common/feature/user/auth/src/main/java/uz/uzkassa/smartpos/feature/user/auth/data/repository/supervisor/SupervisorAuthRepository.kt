package uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor.params.SupervisorAuthenticateParams

internal interface SupervisorAuthRepository {

    fun authenticate(params: SupervisorAuthenticateParams): Flow<Unit>
}