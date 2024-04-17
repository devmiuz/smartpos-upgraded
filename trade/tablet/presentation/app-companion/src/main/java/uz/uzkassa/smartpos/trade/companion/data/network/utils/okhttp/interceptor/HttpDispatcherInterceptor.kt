package uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import uz.uzkassa.smartpos.trade.companion.BuildConfig
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.error.HttpErrorResponseHandler
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.error.HttpErrorResponseMapper
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.interceptor.request.HttpRequestInterceptor
import java.net.ConnectException

internal class HttpDispatcherInterceptor private constructor(
    private val requestInterceptors: Array<HttpRequestInterceptor>,
    private val errorHandlers: Array<HttpErrorResponseHandler>,
    private val errorMappers: Array<HttpErrorResponseMapper>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response?

        try {
            var resultRequest: Request? = null
            requestInterceptors
                .forEach { resultRequest = it.intercept(resultRequest ?: chain.request()) }
            response = chain.proceed(resultRequest ?: chain.request())
        } catch (exception: Exception) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Error when proceeding", exception)
            throw if (exception is IOException) ConnectException() else exception
        }

        val httpErrorCode: Int = response.code

        if (httpErrorCode in 400..503) {
            val errorResponse: HttpErrorResponse = errorMappers.mapNotNull {
                it.onHandleResponse(httpErrorCode, response.peekBody(Long.MAX_VALUE).string())
            }.firstOrNull() ?: getErrorResponse(httpErrorCode)

            val firstAvailableErrorHandler: Throwable? = errorHandlers.mapNotNull {
                it.onHandleError(chain.request().url.toUri(), httpErrorCode, errorResponse)
            }.firstOrNull()

            if (firstAvailableErrorHandler != null) throw firstAvailableErrorHandler
        }

        return response
    }

    private fun getErrorResponse(httpErrorCode: Int): HttpErrorResponse =
        object : HttpErrorResponse {
            override val httpErrorCode: Int = httpErrorCode
        }

    object Builder {
        private val requestInterceptors: MutableList<HttpRequestInterceptor> = arrayListOf()
        private val errorHandlers: MutableList<HttpErrorResponseHandler> = arrayListOf()
        private val errorMappers: MutableList<HttpErrorResponseMapper> = arrayListOf()

        fun setRequestInterceptors(vararg interceptors: HttpRequestInterceptor): Builder {
            requestInterceptors.apply { clear(); addAll(interceptors) }
            return this
        }

        fun setErrorResponseHandlers(vararg handlers: HttpErrorResponseHandler): Builder {
            errorHandlers.apply { clear(); addAll(handlers) }
            return this
        }

        fun setErrorResponseMappers(vararg mappers: HttpErrorResponseMapper): Builder {
            errorMappers.apply { clear(); addAll(mappers) }
            return this
        }

        fun build(): HttpDispatcherInterceptor =
            HttpDispatcherInterceptor(
                requestInterceptors = requestInterceptors.toTypedArray(),
                errorHandlers = errorHandlers.toTypedArray(),
                errorMappers = errorMappers.toTypedArray()
            )
    }

    companion object {
        private const val TAG: String = "Error Interceptor"
    }
}