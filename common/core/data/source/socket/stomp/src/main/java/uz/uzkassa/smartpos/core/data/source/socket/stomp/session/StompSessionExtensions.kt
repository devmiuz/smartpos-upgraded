package uz.uzkassa.smartpos.core.data.source.socket.stomp.session

import uz.uzkassa.smartpos.core.data.source.socket.stomp.destination.StompDestination
import uz.uzkassa.smartpos.core.data.source.socket.stomp.destination.StompDestinationImpl

fun StompSession.toDestination(url: String): StompDestination =
    StompDestinationImpl(this, url)

suspend inline fun <T> StompSession.use(block: StompSession.() -> T): T {
    connect()
    try {
        return block(this)
    } finally {
        disconnect()
    }
}