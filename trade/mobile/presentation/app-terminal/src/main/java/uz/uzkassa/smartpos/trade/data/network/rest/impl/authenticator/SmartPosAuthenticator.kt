package uz.uzkassa.smartpos.trade.data.network.rest.impl.authenticator

import okhttp3.*
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model.RefreshAuthResponse
import uz.uzkassa.smartpos.trade.data.network.rest.impl.service.ServicesHolder
import uz.uzkassa.smartpos.trade.data.network.utils.credential.CurrentCredentialParams

internal class SmartPosAuthenticator(
    private val currentCredentialParams: CurrentCredentialParams,
    private val serviceHolder: ServicesHolder
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val originalRequest: Request = response.request

        if (isDeniedRequest(originalRequest.url)) return null

        if (originalRequest.header("Authorization") != null) {
            synchronized(this) {
                if (originalRequest.header("Authorization") != null) {
                    val tokenResponse: retrofit2.Response<RefreshAuthResponse> =
                        checkNotNull(serviceHolder.refreshAuthRestService)
                            .refreshToken(refreshToken = currentCredentialParams.refreshToken)
                            .execute()

                    if (tokenResponse.isSuccessful)
                        tokenResponse.body()?.let { it ->
                            currentCredentialParams.accessToken = it.accessToken
                            return@let it.refreshToken?.let {
                                currentCredentialParams.refreshToken = it
                            }
                        }
                }
            }
        }

        return response.request
            .newBuilder()
            .header("Authorization", currentCredentialParams.accessToken ?: "")
            .build()
    }

    private fun isDeniedRequest(httpUrl: HttpUrl): Boolean =
        arrayOf("api/login", "api/refresh-token").any { httpUrl.encodedPath.contains(it) }
}