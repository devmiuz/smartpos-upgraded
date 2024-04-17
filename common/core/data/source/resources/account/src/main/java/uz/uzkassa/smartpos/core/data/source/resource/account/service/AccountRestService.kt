package uz.uzkassa.smartpos.core.data.source.resource.account.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.account.model.AccountResponse

interface AccountRestService {

    fun getCurrentAccount(): Flow<AccountResponse>

    companion object {

        fun instantiate(retrofit: Retrofit): AccountRestService =
            AccountRestServiceImpl(retrofit.create())
    }
}