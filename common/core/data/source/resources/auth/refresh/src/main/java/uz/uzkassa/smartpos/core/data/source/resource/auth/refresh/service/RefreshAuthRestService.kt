package uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model.RefreshAuthResponse

interface RefreshAuthRestService {

    fun refreshToken(refreshToken: String): Call<RefreshAuthResponse>

    companion object {

        fun instantiate(retrofit: Retrofit): RefreshAuthRestService =
            RefreshAuthRestServiceImpl(retrofit.create())
    }
}