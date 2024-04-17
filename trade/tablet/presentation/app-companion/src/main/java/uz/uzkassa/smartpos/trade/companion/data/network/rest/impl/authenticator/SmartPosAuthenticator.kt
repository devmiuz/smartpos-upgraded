package uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.authenticator

import okhttp3.*
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model.RefreshAuthResponse
import uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.service.ServicesHolder
import uz.uzkassa.smartpos.trade.companion.data.network.utils.credential.CurrentCredentialParams

internal class SmartPosAuthenticator(
    private val currentCredentialParams: CurrentCredentialParams,
    private val serviceHolder: ServicesHolder
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val originalRequest: Request = response.request

        if (isDeniedRequest(originalRequest.url)) return null

        if (originalRequest.header("Authorization") != null)
            currentCredentialParams.accessToken = null

        if (currentCredentialParams.accessToken == null) {
            synchronized(this) {
                if (currentCredentialParams.accessToken == null) {
                    val tokenResponse: retrofit2.Response<RefreshAuthResponse> =
                        checkNotNull(serviceHolder.refreshAuthRestService)
                            .refreshToken(
                                basicAuth = currentCredentialParams.basicAuth,
                                refreshToken = currentCredentialParams.refreshToken
                            )
                            .execute()

                    if (tokenResponse.isSuccessful)
                        tokenResponse.body().let { it ->
                            currentCredentialParams.accessToken = it?.accessToken
                            return@let it?.refreshToken.let {
                                currentCredentialParams.refreshToken = it ?: ""
                            }
                            // TODO: 7/27/20 fix refresh token
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
        httpUrl.encodedPath.contains("api/login", ignoreCase = true)
}