package uz.uzkassa.apay.data.repository.qr.card.engine

import uz.uzkassa.apay.data.repository.qr.card.engine.apdu.APDUExchange

interface CardHandler {

    fun exchangeCommand(data: APDUExchange.Request): APDUExchange.Response

    val isActive: Boolean

    val uid: String?

    fun powerOff()

    fun powerOn(byteArray: ByteArray)
}