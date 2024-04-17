package uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.error

import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.HttpDispatcherComponent
import java.io.IOException
import java.net.URI

internal interface HttpErrorResponseHandler : HttpDispatcherComponent {

    fun onHandleError(uri: URI, httpErrorCode: Int, errorResponse: HttpErrorResponse): IOException?
}