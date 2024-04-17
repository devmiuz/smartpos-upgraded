package uz.uzkassa.smartpos.core.data.source.socket.stomp.session.header

import org.hildan.krossbow.stomp.headers.StompMessageHeaders

data class StompMessageHeader(
    private val rawHeaders: MutableMap<String, String>,
    val destination: String,
    val messageId: String,
    val subscription: String,
    val ack: String?
) : Map<String, String> by rawHeaders {

    internal companion object {

        fun map(headers: StompMessageHeaders): StompMessageHeader = with(headers) {
            StompMessageHeader(this, destination, messageId, subscription, ack)
        }
    }
}