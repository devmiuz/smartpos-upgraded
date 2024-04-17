package uz.uzkassa.smartpos.core.data.source.resource.auth.user.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.model.UserAuthResponse
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference

interface UserAuthRestService {

    fun authenticate(jsonElement: JsonElement): Flow<UserAuthResponse>

    fun changePassword(jsonElement: JsonElement): Flow<Unit>

    fun logout(): Flow<Unit>

    fun requestCashierNewPinCode(jsonElement: JsonElement): Flow<Unit>

    companion object {

        fun instantiate(
            userAuthPreference: UserAuthPreference,
            retrofit: Retrofit
        ): UserAuthRestService =
            UserAuthRestServiceImpl(
                userAuthPreference = userAuthPreference,
                userAuthRestServiceInternal = retrofit.create()
            )
    }
}