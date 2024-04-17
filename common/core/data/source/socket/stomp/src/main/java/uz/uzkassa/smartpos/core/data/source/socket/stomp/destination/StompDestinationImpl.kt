package uz.uzkassa.smartpos.core.data.source.socket.stomp.destination

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.StompSession
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.message.StompMessage
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.receipt.StompReceipt

internal class StompDestinationImpl(
    private val stompSession: StompSession,
    private val url: String
) : StompDestination {

    override val isConnected: Boolean
        get() = stompSession.isConnected

    override suspend fun send(body: ByteArray): StompReceipt? {
        return stompSession.send(url, body)
    }

    override suspend fun send(message: String?): StompReceipt? {
        return stompSession.send(url, message)
    }

    override suspend fun <T> use(block: suspend StompDestination.() -> T): T {
        stompSession.connect()
        return try {
            block(this)
        } finally {
            stompSession.disconnect()
        }
    }

    override fun subscribe(): Flow<StompMessage> {
        return stompSession.subscribe(url)
    }
}