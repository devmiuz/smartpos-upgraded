package uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.service.RefreshAuthRestService
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.authenticator.SmartPosAuthenticator
import uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.retrofit.adapter.FlowCallAdapterFactory
import uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.retrofit.converter.CustomConvertersFactory
import uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.service.ServicesHolder
import uz.uzkassa.smartpos.trade.companion.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.OkHttpInstance
import java.util.concurrent.TimeUnit

class RetrofitInstance(
    private val currentCredentialParams: CurrentCredentialParams,
    private val okHttpInstance: OkHttpInstance
) {
    private val servicesHolder: ServicesHolder = ServicesHolder()

    private val okHttpClient: OkHttpClient by lazy {
        okHttpInstance.getOkHttpClient {
            val authenticator: Authenticator =
                SmartPosAuthenticator(currentCredentialParams, servicesHolder)

            this.authenticator(authenticator)
                .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    val retrofit: Retrofit by lazy {
        val instance: Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(FlowCallAdapterFactory)
                .addConverterFactory(CustomConvertersFactory)
                .addConverterFactory(Json.actual.asConverterFactory("application/json; charset=utf-8".toMediaType()))
                .build()

        servicesHolder.refreshAuthRestService = RefreshAuthRestService.instantiate(instance)
        return@lazy instance
    }

    private companion object {
        const val BASE_URL: String = "https://api-dev.smartpos.uz/"
        const val CONNECTION_TIMEOUT_SECONDS: Long = 60
    }
}