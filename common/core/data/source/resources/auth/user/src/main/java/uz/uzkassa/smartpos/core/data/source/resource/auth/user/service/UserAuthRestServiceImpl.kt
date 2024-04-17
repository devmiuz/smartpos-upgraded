package uz.uzkassa.smartpos.core.data.source.resource.auth.user.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.content
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.model.UserAuthResponse
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference

internal class UserAuthRestServiceImpl(
    private val userAuthPreference: UserAuthPreference,
    private val userAuthRestServiceInternal: UserAuthRestServiceInternal
) : UserAuthRestService {

    override fun authenticate(jsonElement: JsonElement): Flow<UserAuthResponse> {
        return userAuthRestServiceInternal.authenticate(jsonElement)
            .onEach {
                userAuthPreference.apply {
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
        return userAuthRestServiceInternal.changePassword(jsonElement)
    }

    override fun logout(): Flow<Unit> {
        return flowOf(Unit).onEach {  }
    }

    override fun requestCashierNewPinCode(jsonElement: JsonElement): Flow<Unit> {
        return userAuthRestServiceInternal.requestCashierNewPinCode(jsonElement)
    }
}