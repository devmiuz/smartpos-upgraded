package uz.uzkassa.apay.data.model

import uz.uzkassa.launcher.data.manager.sam.card.model.CardError

class CardResult<T : Any> private constructor(
    private val value: T? = null,
    private val error: CardError? = null
) {

    val isSuccess: Boolean get() = value != null

    val isError: Boolean get() = error != null

    @Suppress("UNCHECKED_CAST")
    fun dataOrNull(): T? =
        when {
            isError -> null
            else -> value
        }

    @Suppress("UNCHECKED_CAST")
    fun errorOrNull(): CardError? =
        when {
            isError -> error
            else -> null
        }

    @Suppress("UNCHECKED_CAST")
    fun dataOrThrow(): T =
        value ?: throw RuntimeException("Unable to get value")

    fun errorOrThrow(): CardError =
        error ?: throw RuntimeException("Unable to get error")

    companion object {

        fun <T : Any> success(value: T?): CardResult<T> =
            CardResult(value, null)

        fun <T : Any> error(error: CardError?): CardResult<T> =
            CardResult(null, error)
    }
}