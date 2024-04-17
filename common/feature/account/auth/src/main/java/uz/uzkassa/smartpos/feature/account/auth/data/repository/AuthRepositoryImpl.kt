package uz.uzkassa.smartpos.feature.account.auth.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import uz.uzkassa.smartpos.core.data.source.resource.account.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.account.model.Account
import uz.uzkassa.smartpos.core.data.source.resource.account.service.AccountRestService
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.feature.account.auth.data.repository.params.AuthenticateParams
import uz.uzkassa.smartpos.feature.account.auth.data.repository.params.RequestRecoveryParams
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val accountAuthRestService: AccountAuthRestService,
    private val accountRestService: AccountRestService
) : AuthRepository {

    @FlowPreview
    override fun authenticate(params: AuthenticateParams): Flow<Account> {
        return accountAuthRestService.authenticate(params.asJsonElement())
            .flatMapConcat {
                accountRestService.getCurrentAccount()
            }
            .map { it.map() }
    }

    override fun requestPasswordRecovery(params: RequestRecoveryParams): Flow<Unit> {
        return accountAuthRestService.requestRecoveryPassword(params.phoneNumber)
    }
}