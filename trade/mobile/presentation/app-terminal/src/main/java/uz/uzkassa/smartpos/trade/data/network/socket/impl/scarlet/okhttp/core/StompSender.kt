/*
 * © 2018 Match Group, LLC.
 */
package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.core

import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.models.StompHeader

/**
 * Operations for sending messages to a destination.
 */
interface StompSender {

    /**
     * Convert the given byte array to serialized form, possibly using a
     * StompMessageEncoder, wrap it as a message and send it to a given destination.
     * @param payload the byte array to use as payload
     * @param destination the target destination
     * @param headers headers for the message to send
     * @see StompMessageEncoder
     */
    fun convertAndSend(payload: ByteArray, destination: String, headers: StompHeader?): Boolean
}