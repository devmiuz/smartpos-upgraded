package uz.uzkassa.smartpos.core.data.source.socket.stomp.session.receipt

import org.hildan.krossbow.stomp.StompReceipt

data class StompReceipt(val id: String) {

    internal constructor(receipt: StompReceipt) : this(receipt.id)
}