package uz.uzkassa.smartpos.core.data.source.gtpos.model.result

import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSException

@Suppress("UNCHECKED_CAST")
class GTPOSResult<out T : Any> internal constructor(
    private val value: Any? = null
) {
    val isError: Boolean
        get() = value is GTPOSError

    val isSuccess: Boolean
        get() = !isError

    fun dataOrThrow(): T {
        if (value is GTPOSError) throw value.exception
        return value as T
    }

    fun errorOrUnknown(): GTPOSException {
        if (isSuccess) return GTPOSException(GTPOSErrorType.UNKNOWN)
        return (value as GTPOSError).exception
    }

    private data class GTPOSError(val exception: GTPOSException)

    companion object {

        fun <T : Any> success(value: T): GTPOSResult<T> =
            GTPOSResult(value)

        fun <T : Any> error(errorType: GTPOSErrorType): GTPOSResult<T> =
            GTPOSResult(GTPOSError(GTPOSException(errorType)))
    }
}