package uz.uzkassa.apay.data.repository.qr.card.engine.type.nexgo

import com.nexgo.oaf.apiv3.card.cpu.APDUEntity
import com.nexgo.oaf.apiv3.card.cpu.CPUCardHandler
import uz.uzkassa.apay.data.repository.qr.card.engine.CardHandler
import uz.uzkassa.apay.data.repository.qr.card.engine.apdu.APDUExchange

class NexGoCardHandler(private val cpuCardHandler: CPUCardHandler?) : CardHandler {

    override fun exchangeCommand(data: APDUExchange.Request): APDUExchange.Response {
        val entity: APDUEntity = data.mapToAPDUEntity()
        cpuCardHandler?.exchangeAPDUCmd(entity)
        return entity.mapToAPDUData()
    }

    override val isActive: Boolean
        get() = cpuCardHandler?.active() ?: false

    override val uid: String?
        get() = cpuCardHandler?.readUid()

    override fun powerOff() {
        cpuCardHandler?.powerOff()
    }

    override fun powerOn(byteArray: ByteArray) {
        cpuCardHandler?.powerOn(byteArray)
    }

    private fun APDUExchange.Request.mapToAPDUEntity() = APDUEntity().apply {
        this.cla = this@mapToAPDUEntity.cla;
        this.ins = this@mapToAPDUEntity.ins;
        this.p1 = this@mapToAPDUEntity.p1;
        this.p2 = this@mapToAPDUEntity.p2;
        this.lc = this@mapToAPDUEntity.lc
        this.dataIn = this@mapToAPDUEntity.dataIn
    }

    private fun APDUEntity.mapToAPDUData(): APDUExchange.Response =
        APDUExchange.Response(p1, p2, lc, le, ins, cla, swa, swb, dataOutLen, dataIn, dataOut)
}