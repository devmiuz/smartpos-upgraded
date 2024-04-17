package uz.uzkassa.smartpos.trade.data.network.utils.interceptor.error

import uz.uzkassa.smartpos.core.data.source.resource.rest.handler.HttpErrorHandler
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import uz.uzkassa.smartpos.trade.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.error.HttpErrorResponseHandler
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI

internal class ErrorResponseHandler(
    private val currentCredentialParams: CurrentCredentialParams
) : HttpErrorResponseHandler {

    override fun onHandleError(
        uri: URI,
        httpErrorCode: Int,
        errorResponse: HttpErrorResponse
    ): IOException? {
        if (httpErrorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            if (!uri.toString().contains("api/login", ignoreCase = true))
                currentCredentialParams.accessToken = null
        }

        return HttpErrorHandler.handle(httpErrorCode, errorResponse)
    }
}