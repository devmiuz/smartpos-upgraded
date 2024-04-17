package uz.uzkassa.smartpos.core.data.source.socket.stomp.session

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.message.StompMessage
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.receipt.StompReceipt

interface StompSession {

    val isConnected: Boolean

    suspend fun connect()

    suspend fun disconnect()

    suspend fun send(url: String, body: ByteArray): StompReceipt?

    suspend fun send(url: String, message: String? = null): StompReceipt?

    fun subscribe(url: String): Flow<StompMessage>
}