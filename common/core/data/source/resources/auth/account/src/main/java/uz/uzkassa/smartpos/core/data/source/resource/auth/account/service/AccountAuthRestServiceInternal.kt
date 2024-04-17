package uz.uzkassa.smartpos.core.data.source.resource.auth.account.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.POST
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.model.AccountAuthResponse

internal interface AccountAuthRestServiceInternal {

    /// Login
    @POST(API_LOGIN)
    fun authenticate(@Body jsonElement: JsonElement): Flow<AccountAuthResponse>

    /// Password
    @POST(API_CHANGE_PASSWORD)
    fun changePassword(@Body jsonElement: JsonElement): Flow<Unit>

    @POST(API_RESET_PASSWORD_START)
    fun requestRecoveryPassword(@Body phoneNumber: Long): Flow<Unit>

    @POST(API_RESET_PASSWORD_FINISH)
    fun finishRecoveryPassword(@Body jsonElement: JsonElement): Flow<Unit>

    /// Registration
    @POST(API_ACTIVATE_SMS)
    fun activateRegistrationBySms(@Body jsonElement: JsonElement): Flow<Unit>

    @POST(API_REGISTER)
    fun requestRegistrationConfirmationSmsCode(@Body jsonElement: JsonElement): Flow<Unit>

    private companion object {
        const val API_ACTIVATE_SMS: String = "api/activate-sms"
        const val API_CHANGE_PASSWORD: String = "api/account/change-password"
        const val API_LOGIN: String = "api/login/mobile"
        const val API_REGISTER: String = "api/register"
        const val API_RESET_PASSWORD_START: String = "api/account/reset-password/init"
        const val API_RESET_PASSWORD_FINISH: String = "api/account/reset-password/finish"
    }
}