package uz.uzkassa.smartpos.core.data.source.socket.stomp.session.message

import org.hildan.krossbow.stomp.frame.StompFrame
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.body.StompBody
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.header.StompMessageHeader
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.state.StompState

data class StompMessage(
    val header: StompMessageHeader,
    val body: StompBody,
    val state: StompState
) {

    internal constructor(
        message: StompFrame.Message
    ) : this(
        header = StompMessageHeader.map(message.headers),
        body = StompBody.map(message.body),
        state = StompState.map(message.command)
    )
}