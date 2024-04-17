package uz.uzkassa.smartpos.core.data.source.resource.account.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.account.model.AccountResponse

internal interface AccountRestServiceInternal {

    @GET(API_ACCOUNT)
    fun getCurrentAccount(): Flow<AccountResponse>

    private companion object {
        const val API_ACCOUNT: String = "api/account"
    }
}