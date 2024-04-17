package uz.uzkassa.smartpos.trade.data.network.utils.interceptor.header

import okhttp3.HttpUrl
import okhttp3.Request
import uz.uzkassa.smartpos.trade.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.request.HttpRequestInterceptor

internal class HeaderTokenInterceptor(
    private val currentCredentialParams: CurrentCredentialParams
) : HttpRequestInterceptor {

    override fun intercept(request: Request): Request {
        return request.newBuilder()
            .apply {
                if (isAllowedRequest(request.url))
                    currentCredentialParams.accessToken.let {
                        if (!it.isNullOrBlank()) addHeader("Authorization", it)
                    }
            }
            .url(request.url)
            .build()
    }

    private fun isAllowedRequest(httpUrl: HttpUrl): Boolean =
        arrayOf("Authorization", "refresh_token")
            .any { httpUrl.encodedPath.contains(it, ignoreCase = true) }
}