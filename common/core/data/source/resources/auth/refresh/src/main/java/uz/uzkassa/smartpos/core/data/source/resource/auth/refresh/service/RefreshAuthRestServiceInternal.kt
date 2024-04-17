package uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.service

import kotlinx.serialization.json.JsonElement
import retrofit2.Call
import retrofit2.http.*
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model.RefreshAuthResponse

internal interface RefreshAuthRestServiceInternal {

    @PUT(API_REFRESH_TOKEN)
    fun refreshToken(
        @Body jsonElement: JsonElement
    ): Call<RefreshAuthResponse>

    private companion object {
        const val API_REFRESH_TOKEN: String = "/api/refresh-token"
    }
}