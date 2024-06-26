
package uz.uzkassa.smartpos.core.data.source.resource.store.data;

/**
 * Holder for responses from Store.
 *
 * Instead of using regular error channels (a.k.a. throwing exceptions), Store uses this holder
 * class to represent each response. This allows the flow to keep running even if an error happens
 * so that if there is an observable single source of truth, application can keep observing it.
 */
sealed class StoreResponse<out T> {
    /**
     * Represents the source of the Response.
     */
    abstract val origin: ResponseOrigin

    /**
     * Loading event dispatched by [Store] to signal the [Fetcher] is in progress.
     */
    data class Loading<T>(override val origin: ResponseOrigin) : StoreResponse<T>()

    /**
     * Data dispatched by [Store]
     */
    data class Data<T>(val value: T, override val origin: ResponseOrigin) : StoreResponse<T>()

    /**
     * No new data event dispatched by Store to signal the [Fetcher] returned no data (i.e the
     * returned [kotlinx.coroutines.Flow], when collected, was empty).
     */
    data class NoNewData<T>(override val origin: ResponseOrigin) : StoreResponse<T>()

    /**
     * Error dispatched by a pipeline
     */
    sealed class Error<T> : StoreResponse<T>() {
        data class Exception<T>(
            val error: Throwable,
            override val origin: ResponseOrigin
        ) : Error<T>()

        data class Message<T>(
            val message: String,
            override val origin: ResponseOrigin
        ) : Error<T>()
    }

    /**
     * Returns the available data or throws [NullPointerException] if there is no data.
     */
    fun requireData(): T {
        return when (this) {
            is Data -> value
            is Error -> this.doThrow()
            else -> throw NullPointerException("there is no data in $this")
        }
    }

    /**
     * If this [StoreResponse] is of type [StoreResponse.Error], throws the exception
     * Otherwise, does nothing.
     */
    fun throwIfError() {
        if (this is Error) {
            this.doThrow()
        }
    }

    /**
     * If this [StoreResponse] is of type [StoreResponse.Error], returns the available error
     * from it. Otherwise, returns `null`.
     */
    fun errorMessageOrNull(): String? {
        return when (this) {
            is Error.Message -> message
            is Error.Exception -> error.localizedMessage ?: "exception: ${error.javaClass}"
            else -> null
        }
    }

    /**
     * If there is data available, returns it; otherwise returns null.
     */
    fun dataOrNull(): T? = when (this) {
        is Data -> value
        else -> null
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <R> swapType(): StoreResponse<R> = when (this) {
        is Error -> this as Error<R>
        is Loading -> this as Loading<R>
        is NoNewData -> this as NoNewData<R>
        is Data -> throw RuntimeException("cannot swap type for StoreResponse.Data")
    }
}

/**
 * Represents the origin for a [StoreResponse].
 */
enum class ResponseOrigin {
    /**
     * [StoreResponse] is sent from the cache
     */
    Cache,

    /**
     * [StoreResponse] is sent from the persister
     */
    SourceOfTruth,

    /**
     * [StoreResponse] is sent from a fetcher,
     */
    Fetcher
}

fun <T> StoreResponse.Error<T>.doThrow(): Nothing = when (this) {
    is StoreResponse.Error.Exception -> throw error
    is StoreResponse.Error.Message -> throw RuntimeException(message)
}
