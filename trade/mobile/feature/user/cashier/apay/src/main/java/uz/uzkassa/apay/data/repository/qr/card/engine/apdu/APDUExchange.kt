package uz.uzkassa.apay.data.repository.qr.card.engine.apdu

import kotlinx.serialization.Serializable

class APDUExchange {

    @Serializable
    data class Request(
        val cla: Byte,
        val lc: Int,
        val le: Int = -1,
        val ins: Byte,
        val p1: Byte,
        val p2: Byte,
        val dataIn: ByteArray = byteArrayOf(256.toByte())
    ) {
        constructor(
            cla: Int,
            lc: Int = 0,
            le: Int = -1,
            ins: Int,
            p1: Int,
            p2: Int,
            dataIn: ByteArray = byteArrayOf(256.toByte())
        ) : this(cla.toByte(), lc, le, ins.toByte(), p1.toByte(), p2.toByte(), dataIn)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Request

            if (cla != other.cla) return false
            if (ins != other.ins) return false
            if (p1 != other.p1) return false
            if (p2 != other.p2) return false
            if (lc != other.lc) return false
            if (!dataIn.contentEquals(other.dataIn)) return false

            return true
        }

        override fun hashCode(): Int {
            var result: Byte = cla.toByte()
            result = (31 * result + ins).toByte()
            result = (31 * result + p1).toByte()
            result = (31 * result + p2).toByte()
            result = (31 * result + lc).toByte()
            result = (31 * result + dataIn.contentHashCode()).toByte()
            return result.hashCode()
        }
    }

    @Serializable
    data class Response(
        val p1: Byte,
        val p2: Byte,
        val lc: Int,
        val le: Int = -1,
        val ins: Byte,
        val cla: Byte,
        val swa: Byte,
        val swb: Byte,
        val dataOutLen: Int,
        val dataIn: ByteArray = byteArrayOf(256.toByte()),
        val dataOut: ByteArray = byteArrayOf(256.toByte())
    ) {


        constructor(
            swa: Byte,
            swb: Byte
        ) : this(
            p1 = 0.toByte(),
            p2 = 0.toByte(),
            lc = -1,
            le = -1,
            ins = 0.toByte(),
            cla = 0.toByte(),
            swa = swa,
            swb = swb,
            dataOutLen = 0,
            dataIn = byteArrayOf(256.toByte()),
            dataOut = byteArrayOf(256.toByte())
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Response

            if (p1 != other.p1) return false
            if (p2 != other.p2) return false
            if (lc != other.lc) return false
            if (le != other.le) return false
            if (ins != other.ins) return false
            if (cla != other.cla) return false
            if (swa != other.swa) return false
            if (swb != other.swb) return false
            if (dataOutLen != other.dataOutLen) return false
            if (!dataIn.contentEquals(other.dataIn)) return false
            if (!dataOut.contentEquals(other.dataOut)) return false

            return true
        }

        override fun hashCode(): Int {
            var result: Byte = p1
            result = (31 * result + p2).toByte()
            result = (31 * result + lc).toByte()
            result = (31 * result + le).toByte()
            result = (31 * result + ins).toByte()
            result = (31 * result + cla).toByte()
            result = (31 * result + swa).toByte()
            result = (31 * result + swb).toByte()
            result = (31 * result + dataOutLen).toByte()
            result = (31 * result + dataIn.contentHashCode()).toByte()
            result = (31 * result + dataOut.contentHashCode()).toByte()
            return result.hashCode()
        }
    }
}