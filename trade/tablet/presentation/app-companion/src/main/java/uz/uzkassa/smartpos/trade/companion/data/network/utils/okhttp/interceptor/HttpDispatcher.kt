package uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor

import okhttp3.OkHttpClient

fun OkHttpClient.Builder.addHttpDispatcherInterceptor(
    vararg components: HttpDispatcherComponent
): OkHttpClient.Builder = addInterceptor(
    HttpDispatcherInterceptor.Builder.build()
)