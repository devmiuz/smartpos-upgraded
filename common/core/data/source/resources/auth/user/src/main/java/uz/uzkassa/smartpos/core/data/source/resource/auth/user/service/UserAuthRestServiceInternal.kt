package uz.uzkassa.smartpos.core.data.source.resource.auth.user.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.POST
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.model.UserAuthResponse

internal interface UserAuthRestServiceInternal {

    @POST(API_LOGIN)
    fun authenticate(@Body jsonElement: JsonElement): Flow<UserAuthResponse>

    @POST(API_CHANGE_PASSWORD)
    fun changePassword(@Body jsonElement: JsonElement): Flow<Unit>

    @POST(API_RESET_PIN_CASHIER)
    fun requestCashierNewPinCode(@Body jsonElement: JsonElement): Flow<Unit>

    private companion object {
        const val API_CHANGE_PASSWORD: String = "api/account/change-password"
        const val API_LOGIN: String = "api/login/mobile"
        const val API_RESET_PIN_CASHIER: String = "api/account/reset-pin/cashier"
    }
}