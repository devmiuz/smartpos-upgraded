/*
 * © 2018 Match Group, LLC.
 */
package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.core

import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.models.StompHeader
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.models.StompMessage

typealias StompListener = (StompMessage) -> Unit

/**
 * Interface use for subscribe and unsubscribe to STOMP queue.
 */
interface StompSubscriber {

    /**
     * Subscribe to given destination with headers.
     */
    fun subscribe(destination: String, headers: StompHeader?, listener: StompListener)

    /**
     * Unsubscribe from given destination.
     */
    fun unsubscribe(destination: String)
}