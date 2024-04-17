package uz.uzkassa.smartpos.core.utils.coroutines.channels

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.selects.SelectClause2

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
open class BroadcastChannelWrapper<E>(
    private val channel: BroadcastChannel<E>
) : BroadcastChannel<E> {

    constructor(capacity: Int) : this(BroadcastChannel(capacity))

    constructor() : this(BroadcastChannel(1))

    override val isClosedForSend: Boolean
        get() = channel.isClosedForSend

    override val isFull: Boolean
        get() = throw UnsupportedOperationException()

    override val onSend: SelectClause2<E, SendChannel<E>>
        get() = channel.onSend

    override fun cancel(cause: Throwable?): Boolean =
        throw UnsupportedOperationException()

    override fun cancel(cause: CancellationException?) =
        channel.cancel(cause)

    override fun close(cause: Throwable?): Boolean =
        channel.close(cause)

    override fun invokeOnClose(handler: (cause: Throwable?) -> Unit) =
        channel.invokeOnClose(handler)

    override fun offer(element: E): Boolean =
        channel.offer(element)

    override fun openSubscription(): ReceiveChannel<E> =
        channel.openSubscription()

    override suspend fun send(element: E) =
        channel.send(element)
}

//@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
//open class BroadcastChannelWrapper<E : Any>(private val capacity: Int = 1) {
//    private val stateFlow: MutableStateFlow<E?> = MutableStateFlow(null)
//
//    open fun asFlow(): Flow<E> =
//        flowOf(Unit)
//            .flatMapMerge {
//                stateFlow.debounce(1)
//                    .onEach {
//                        if (it != null && capacity != Channel.CONFLATED)
//                            stateFlow.value = null
//                    }
//                    .filterNotNull()
//            }
//
//    fun send(element: E) =
//        sendBlocking(element)
//
//    fun sendBlocking(element: E) {
//        stateFlow.value = element
//    }
//}