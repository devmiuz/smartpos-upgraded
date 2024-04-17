package uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import uz.uzkassa.smartpos.trade.companion.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.companion.data.network.utils.interceptor.error.ErrorResponseHandler
import uz.uzkassa.smartpos.trade.companion.data.network.utils.interceptor.error.ErrorResponseMapper
import uz.uzkassa.smartpos.trade.companion.data.network.utils.interceptor.header.HeaderTokenInterceptor
import uz.uzkassa.smartpos.trade.companion.data.network.utils.interceptor.logging.DebugHttpLoggingInterceptor
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.HttpDispatcherComponent
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.addHttpDispatcherInterceptor
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.ssl.UnsafeSSL.getUnsafeHostnameVerifier
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.ssl.UnsafeSSL.getUnsafeSSLSocketFactory
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.ssl.UnsafeSSL.getUnsafeTrustManager

class OkHttpInstance(
    currentCredentialParams: CurrentCredentialParams
) {

    private val dispatcherComponents: MutableList<HttpDispatcherComponent> by lazy {
        arrayListOf(
            HeaderTokenInterceptor(currentCredentialParams),
            ErrorResponseHandler(currentCredentialParams),
            ErrorResponseMapper()
        )
    }
    private val interceptors: MutableList<Interceptor> by lazy {
        arrayListOf<Interceptor>(DebugHttpLoggingInterceptor())
    }

    fun getOkHttpClient(
        vararg component: HttpDispatcherComponent = emptyArray(),
        builder: OkHttpClient.Builder.() -> Unit
    ): OkHttpClient {
        val components: Array<HttpDispatcherComponent> =
            dispatcherComponents.apply { addAll(component.asList()) }.toTypedArray()
        return OkHttpClient.Builder()
            .apply { interceptors().addAll(interceptors) }
            .addHttpDispatcherInterceptor(*components)
            .sslSocketFactory(getUnsafeSSLSocketFactory(), getUnsafeTrustManager())
            .hostnameVerifier(getUnsafeHostnameVerifier())
            .retryOnConnectionFailure(false)
            .followRedirects(false)
            .followSslRedirects(false)
            .apply(builder)
            .build()
    }
}