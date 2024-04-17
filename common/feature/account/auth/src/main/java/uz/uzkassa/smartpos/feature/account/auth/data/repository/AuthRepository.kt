package uz.uzkassa.smartpos.feature.account.auth.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.account.model.Account
import uz.uzkassa.smartpos.feature.account.auth.data.repository.params.AuthenticateParams
import uz.uzkassa.smartpos.feature.account.auth.data.repository.params.RequestRecoveryParams

internal interface AuthRepository {

    fun authenticate(params: AuthenticateParams): Flow<Account>

    fun requestPasswordRecovery(params: RequestRecoveryParams): Flow<Unit>
}