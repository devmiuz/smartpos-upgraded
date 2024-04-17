package uz.uzkassa.smartpos.trade.data.network.utils.interceptor.logging

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import uz.uzkassa.smartpos.core.manager.logger.Logger
import uz.uzkassa.smartpos.trade.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor as OkHttpLoggingInterceptor

internal class HttpLoggingInterceptor : Interceptor {
    private val httpLoggingInterceptor: OkHttpLoggingInterceptor =
        OkHttpLoggingInterceptor(HttpLogger)
            .apply { level = if (BuildConfig.DEBUG) BODY else NONE }

    override fun intercept(chain: Interceptor.Chain): Response =
        httpLoggingInterceptor.intercept(chain)

    private object HttpLogger : OkHttpLoggingInterceptor.Logger {
        private const val TAG: String = "Http"

        override fun log(message: String) {
            if (message.isBlank()) return
            when {
                message.contains("socket", ignoreCase = true) ->
                    Log.d(TAG, message)
                message.startsWith("-->") -> {
                    if (message.removePrefix("--> ").substring(0, 3) != "END")
                        Logger.d(TAG, message)
                }
                message.startsWith("<--") -> {
                    val status: String = message.removePrefix("<-- ").substring(0, 3)
                    if (status == "END") return

                    else when (status.toIntOrNull()) {
                        200 -> Logger.d(TAG, message)
                        else -> Logger.e(TAG, message)
                    }
                }
                else -> Log.d(TAG, message)
            }
        }
    }
}