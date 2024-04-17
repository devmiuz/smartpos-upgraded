package uz.uzkassa.smartpos.core.data.source.resource.account.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.account.model.AccountResponse

internal class AccountRestServiceImpl(
    private val restServiceInternal: AccountRestServiceInternal
) : AccountRestService {

    override fun getCurrentAccount(): Flow<AccountResponse> {
        return restServiceInternal.getCurrentAccount()
    }
}