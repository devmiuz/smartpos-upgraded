package uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.service

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import retrofit2.Call
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model.RefreshAuthResponse
import uz.uzkassa.smartpos.core.utils.kserialization.json.create

internal class RefreshAuthRestServiceImpl(
    private val restServiceInternal: RefreshAuthRestServiceInternal
) : RefreshAuthRestService {

    override fun refreshToken(
        refreshToken: String
    ): Call<RefreshAuthResponse> {
        return restServiceInternal
            .refreshToken(JsonObject.create("refresh_token" to JsonPrimitive(refreshToken)))
    }
}