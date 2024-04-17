package uz.uzkassa.smartpos.trade.data.network.utils.okhttp

import okhttp3.OkHttpClient
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.trade.data.network.rest.impl.authenticator.SmartPosAuthenticator
import uz.uzkassa.smartpos.trade.data.network.rest.impl.service.ServicesHolder
import uz.uzkassa.smartpos.trade.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.data.network.utils.interceptor.details.HeaderDetailsInterceptor
import uz.uzkassa.smartpos.trade.data.network.utils.interceptor.error.ErrorResponseHandler
import uz.uzkassa.smartpos.trade.data.network.utils.interceptor.error.ErrorResponseMapper
import uz.uzkassa.smartpos.trade.data.network.utils.interceptor.header.HeaderTokenInterceptor
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.HttpDispatcherComponent
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.addHttpDispatcherInterceptor
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.ssl.UnsafeSSL.getUnsafeHostnameVerifier
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.ssl.UnsafeSSL.getUnsafeSSLSocketFactory
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.ssl.UnsafeSSL.getUnsafeTrustManager
import javax.inject.Inject

class OkHttpInstance @Inject constructor(
    currentBranchPreference: CurrentBranchPreference,
    currentCredentialParams: CurrentCredentialParams,
    deviceInfoManager: DeviceInfoManager,
    servicesHolder: ServicesHolder
) {
    private val authenticator: SmartPosAuthenticator =
        SmartPosAuthenticator(currentCredentialParams, servicesHolder)

    private val dispatcherComponents: List<HttpDispatcherComponent> by lazy {
        listOf(
            HeaderDetailsInterceptor(currentBranchPreference, deviceInfoManager),
            HeaderTokenInterceptor(currentCredentialParams),
            ErrorResponseHandler(currentCredentialParams),
            ErrorResponseMapper()
        )
    }

    fun getOkHttpClient(
        vararg component: HttpDispatcherComponent = emptyArray(),
        builder: OkHttpClient.Builder.() -> Unit
    ): OkHttpClient {
        val components: Array<HttpDispatcherComponent> =
            dispatcherComponents.toMutableList().apply { addAll(component.asList()) }.toTypedArray()

        return OkHttpClient.Builder()
            .apply(builder)
            .addHttpDispatcherInterceptor(*components)
            .authenticator(authenticator)
            .sslSocketFactory(getUnsafeSSLSocketFactory(), getUnsafeTrustManager())
            .hostnameVerifier(getUnsafeHostnameVerifier())
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }
}