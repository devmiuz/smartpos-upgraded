package uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor

import okhttp3.OkHttpClient
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.error.HttpErrorResponseHandler as ResponseHandler
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.error.HttpErrorResponseMapper as ResponseMapper
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.request.HttpRequestInterceptor as RequestInterceptor

fun OkHttpClient.Builder.addHttpDispatcherInterceptor(
    vararg components: HttpDispatcherComponent
): OkHttpClient.Builder = addInterceptor(
    HttpDispatcherInterceptor.Builder
        .setRequestInterceptors(*components.filterIsInstance<RequestInterceptor>().toTypedArray())
        .setErrorResponseHandlers(*components.filterIsInstance<ResponseHandler>().toTypedArray())
        .setErrorResponseMappers(*components.filterIsInstance<ResponseMapper>().toTypedArray())
        .build()
)