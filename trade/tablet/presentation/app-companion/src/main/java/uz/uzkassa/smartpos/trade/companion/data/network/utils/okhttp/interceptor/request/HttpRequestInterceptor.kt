package uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.request

import okhttp3.Request
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.HttpDispatcherComponent

internal interface HttpRequestInterceptor : HttpDispatcherComponent {

    fun intercept(request: Request): Request
}