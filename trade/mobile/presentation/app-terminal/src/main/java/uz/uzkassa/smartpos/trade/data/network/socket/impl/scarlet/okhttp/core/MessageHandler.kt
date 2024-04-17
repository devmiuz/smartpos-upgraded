/*
 * © 2018 Match Group, LLC.
 */
package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.core

import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.models.StompMessage

interface MessageHandler {

    /**
     * Convert given raw data byte array to stomp message
     */
    fun handle(data: ByteArray): StompMessage?
}