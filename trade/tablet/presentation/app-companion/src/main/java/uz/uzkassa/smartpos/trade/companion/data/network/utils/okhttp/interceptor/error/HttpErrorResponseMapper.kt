package uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.error

import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.HttpDispatcherComponent

internal interface HttpErrorResponseMapper : HttpDispatcherComponent {

    fun onHandleResponse(httpErrorCode: Int, response: String?): HttpErrorResponse?
}