/*
 * © 2018 Match Group, LLC.
 */
package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.core

interface IdGenerator {

    /**
     * Generate a new identifier.
     */
    fun generateId(): String
}