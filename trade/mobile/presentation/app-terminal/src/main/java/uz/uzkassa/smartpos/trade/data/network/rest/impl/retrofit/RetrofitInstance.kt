package uz.uzkassa.smartpos.trade.data.network.rest.impl.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.service.RefreshAuthRestService
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.trade.BuildConfig.DEBUG
import uz.uzkassa.smartpos.trade.data.network.rest.impl.retrofit.adapter.FlowCallAdapterFactory
import uz.uzkassa.smartpos.trade.data.network.rest.impl.retrofit.converter.CustomConvertersFactory
import uz.uzkassa.smartpos.trade.data.network.rest.impl.service.ServicesHolder
import uz.uzkassa.smartpos.trade.data.network.utils.interceptor.logging.HttpLoggingInterceptor
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.OkHttpInstance
import java.util.concurrent.TimeUnit

class RetrofitInstance(
    private val okHttpInstance: OkHttpInstance,
    private val servicesHolder: ServicesHolder
) {
    private val okHttpClient: OkHttpClient by lazy {
        okHttpInstance.getOkHttpClient {
            this.addInterceptor(HttpLoggingInterceptor())
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
        val BASE_URL = if (DEBUG) "https://api-dev.smartpos.uz/" else "https://api.smartpos.uz/"

        //        val BASE_URL = "https://api.smartpos.uz/"
//        val BASE_URL = "http://192.168.10.26:8080/"
        const val CONNECTION_TIMEOUT_SECONDS: Long = 60
    }
}