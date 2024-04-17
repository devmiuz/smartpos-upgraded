package uz.uzkassa.smartpos.core.data.source.socket.stomp.destination

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.message.StompMessage
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.receipt.StompReceipt

interface StompDestination {

    val isConnected: Boolean

    suspend fun send(body: ByteArray): StompReceipt?

    suspend fun send(message: String? = null): StompReceipt?

    suspend fun <T> use(block: suspend StompDestination.() -> T): T

    fun subscribe(): Flow<StompMessage>
}