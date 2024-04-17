package uz.uzkassa.smartpos.core.data.source.socket.stomp.session.state

import org.hildan.krossbow.stomp.frame.StompCommand

enum class StompState {
    STOMP,
    CONNECT,
    CONNECTED,
    SEND,
    SUBSCRIBE,
    UNSUBSCRIBE,
    ACK,
    NACK,
    BEGIN,
    COMMIT,
    ABORT,
    DISCONNECT,
    MESSAGE,
    RECEIPT,
    ERROR;

    internal companion object {

        fun map(command: StompCommand): StompState = with(command) {
            return@with when (command) {
                StompCommand.STOMP -> STOMP
                StompCommand.CONNECT -> CONNECT
                StompCommand.CONNECTED -> CONNECTED
                StompCommand.SEND -> SEND
                StompCommand.SUBSCRIBE -> SUBSCRIBE
                StompCommand.UNSUBSCRIBE -> UNSUBSCRIBE
                StompCommand.ACK -> ACK
                StompCommand.NACK -> NACK
                StompCommand.BEGIN -> BEGIN
                StompCommand.COMMIT -> COMMIT
                StompCommand.ABORT -> ABORT
                StompCommand.DISCONNECT -> DISCONNECT
                StompCommand.MESSAGE -> MESSAGE
                StompCommand.RECEIPT -> RECEIPT
                StompCommand.ERROR -> ERROR
            }
        }
    }
}