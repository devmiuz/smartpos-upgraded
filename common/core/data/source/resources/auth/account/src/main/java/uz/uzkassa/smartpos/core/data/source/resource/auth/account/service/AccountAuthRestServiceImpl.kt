package uz.uzkassa.smartpos.core.data.source.resource.auth.account.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.content
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.model.AccountAuthResponse
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference

internal class AccountAuthRestServiceImpl(
    private val accountAccountAuthPreference: AccountAuthPreference,
    private val accountAuthRestServiceInternal: AccountAuthRestServiceInternal
) : AccountAuthRestService {

    override fun authenticate(jsonElement: JsonElement): Flow<AccountAuthResponse> {
        return accountAuthRestServiceInternal.authenticate(jsonElement)
            .onEach {
                accountAccountAuthPreference.apply {
                    accessToken = it.accessToken
                    it.refreshToken?.let { refreshToken = it }
                    setBasicAuth(
                        phoneNumber = checkNotNull(jsonElement.jsonObject["username"]).content,
                        password = checkNotNull(jsonElement.jsonObject["password"]).content
                    )
                }
            }
    }

    override fun changePassword(jsonElement: JsonElement): Flow<Unit> {
        return accountAuthRestServiceInternal.changePassword(jsonElement)
    }

    override fun requestRecoveryPassword(phoneNumber: String): Flow<Unit> {
        return accountAuthRestServiceInternal.requestRecoveryPassword(phoneNumber.toLong())
    }

    override fun finishRecoveryPassword(jsonElement: JsonElement): Flow<Unit> {
        return accountAuthRestServiceInternal.finishRecoveryPassword(jsonElement)
    }

    override fun activateRegistrationBySms(jsonElement: JsonElement): Flow<Unit> {
        return accountAuthRestServiceInternal.activateRegistrationBySms(jsonElement)
    }

    override fun requestRegistrationConfirmationSmsCode(jsonElement: JsonElement): Flow<Unit> {
        return accountAuthRestServiceInternal.requestRegistrationConfirmationSmsCode(jsonElement)
    }
}