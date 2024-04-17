package uz.uzkassa.smartpos.trade.companion.data.network.utils.interceptor.logging

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import uz.uzkassa.smartpos.trade.companion.BuildConfig

internal class DebugHttpLoggingInterceptor : Interceptor {
    private val httpLoggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) BODY else NONE)

    override fun intercept(chain: Interceptor.Chain): Response =
        httpLoggingInterceptor.intercept(chain)
}