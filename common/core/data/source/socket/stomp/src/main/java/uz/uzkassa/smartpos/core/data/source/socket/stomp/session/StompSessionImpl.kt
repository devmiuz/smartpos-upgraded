package uz.uzkassa.smartpos.core.data.source.socket.stomp.session

import kotlinx.coroutines.flow.*
import org.hildan.krossbow.stomp.*
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.message.StompMessage
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.receipt.StompReceipt
import java.io.Closeable
import org.hildan.krossbow.stomp.StompReceipt as KStompReceipt
import org.hildan.krossbow.stomp.StompSession as KStompSession

@Suppress("EXPERIMENTAL_API_USAGE")
internal class StompSessionImpl(
    private val stompClient: StompClient,
    private val url: String,
    private val login: String?,
    private val password: String?
) : StompSession, Closeable {
    private val stateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var session: KStompSession? = null

    override val isConnected: Boolean
        get() = session != null

    override suspend fun connect() {
        session = stompClient.connect(url, login, password)
        stateFlow.value = true
    }

    override suspend fun disconnect() {
        stateFlow.value = false

        try {
            session?.disconnect()
        } finally {
            session = null
        }
    }

    override suspend fun send(url: String, body: ByteArray): StompReceipt? {
        return session?.sendBinary(url, body)?.let { StompReceipt(it) }
    }

    override suspend fun send(url: String, message: String?): StompReceipt? {
        val receipt: KStompReceipt? = if (message.isNullOrBlank()) session?.sendEmptyMsg(url)
        else session?.sendText(url, message)
        return receipt?.let { StompReceipt(it) }
    }

    override fun subscribe(url: String): Flow<StompMessage> {
        return stateFlow
            .flatMapMerge { isConnected ->
                return@flatMapMerge if (isConnected)
                    checkNotNull(session).subscribe(url).map { StompMessage(it) }
                else emptyFlow()
            }
    }

    override fun close() {
        session = null
    }
}