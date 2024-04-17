package uz.uzkassa.smartpos.core.presentation.support.error

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.device.exception.DeviceInfoException
import uz.uzkassa.smartpos.core.data.manager.printer.exception.PrinterException
import uz.uzkassa.smartpos.core.data.source.fiscal.exception.FiscalSourceException
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSException
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.HttpException
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.RestErrorResponse
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.get
import java.net.ConnectException

fun Throwable?.asErrorContent(context: Context): ErrorContent =
    ErrorContent.getErrorContent(context, this)

data class ErrorContent(
    val type: CharSequence? = null,
    val message: CharSequence,
    val details: CharSequence? = null,
    val code: CharSequence? = null
) {

    val isNullContent: Boolean
        get() = type == null && details == null && code == null

    internal companion object {

        fun getErrorContent(context: Context, throwable: Throwable?): ErrorContent {
            val safetyThrowable: Throwable = getSafetyThrowable(throwable)
                ?: return ErrorContent(message = context.getString(R.string.data_source_unknown_error))

            val type: CharSequence = safetyThrowable::class.java.simpleName
                .replace("Exception", "", true)

            val message: CharSequence =
                (safetyThrowable as? LocalizableResource)?.resourceString?.get(context)
                    ?: context.getString(R.string.data_source_unknown_error)

            return when (safetyThrowable) {
                is ConnectException ->
                    ErrorContent(message = context.getString(R.string.data_source_http_error_connection))
                is FiscalSourceException ->
                    ErrorContent(
                        type = type,
                        message = message,
                        code = safetyThrowable.fiscalErrorType::class.java.simpleName
                    )
                is GTPOSException ->
                    ErrorContent(
                        type = type,
                        message = message,
                        code = safetyThrowable.gtposErrorType::class.java.simpleName
                    )
                is HttpException -> when (safetyThrowable.response) {
                    is RestErrorResponse -> {
                        val response: RestErrorResponse =
                            safetyThrowable.response as RestErrorResponse

                        ErrorContent(
                            type = type,
                            message = message,
                            details = response.detail ?: (response.message ?: response.title),
                            code = response.httpErrorCode.toString()
                        )
                    }
                    else -> ErrorContent(
                        type = type,
                        message = message,
                        code = safetyThrowable.response.httpErrorCode.toString()
                    )
                }
                is PrinterException ->
                    ErrorContent(
                        type = type,
                        message = message,
                        code = safetyThrowable.printerErrorType::class.java.simpleName
                    )
                is DeviceInfoException ->
                    ErrorContent(
                        type = type,
                        message = message,
                        details = safetyThrowable.deviceInfoErrorType::class.java.simpleName
                    )
                else -> ErrorContent(type, message)
            }
        }

        private fun getSafetyThrowable(throwable: Throwable?): Throwable? {
            if (throwable == null) return null
            val isExists: Boolean =
                runCatching { Class.forName(throwable.javaClass.name) }
                    .map { true }
                    .getOrElse { false }
            return if (isExists) throwable else null
        }
    }
}