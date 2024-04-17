package uz.uzkassa.smartpos.core.data.source.resource.auth.account.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.model.AccountAuthResponse
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference

interface AccountAuthRestService {

    /// Login
    fun authenticate(jsonElement: JsonElement): Flow<AccountAuthResponse>

    /// Password
    fun changePassword(jsonElement: JsonElement): Flow<Unit>

    fun requestRecoveryPassword(phoneNumber: String): Flow<Unit>

    fun finishRecoveryPassword(jsonElement: JsonElement): Flow<Unit>

    /// Registration
    fun activateRegistrationBySms(jsonElement: JsonElement): Flow<Unit>

    fun requestRegistrationConfirmationSmsCode(jsonElement: JsonElement): Flow<Unit>

    companion object {

        fun instantiate(
            accountAuthPreference: AccountAuthPreference,
            retrofit: Retrofit
        ): AccountAuthRestService =
            AccountAuthRestServiceImpl(
                accountAccountAuthPreference = accountAuthPreference,
                accountAuthRestServiceInternal = retrofit.create()
            )
    }
}