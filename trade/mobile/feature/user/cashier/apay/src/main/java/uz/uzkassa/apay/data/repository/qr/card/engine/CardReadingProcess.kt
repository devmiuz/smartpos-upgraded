package uz.uzkassa.apay.data.repository.qr.card.engine

import com.nexgo.common.ByteUtils
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.repository.qr.card.engine.apdu.APDUExchange
import uz.uzkassa.apay.data.repository.qr.card.engine.javaemreader.*
import java.io.ByteArrayInputStream

class CardReadingProcess(private val cardHandler: CardHandler) {

    fun getCardInfo(): CardInfo {
        val apduData1 = APDUExchange.Request(
            cla = 0x00,
            lc = 0x08,
            ins = 0xA4,
            p1 = 0x04,
            p2 = 0x00,
            dataIn = byteArrayOf(0x45.toByte(), 0x4F, 0x50, 0x43, 0x43, 0x41, 0x52, 0x44)
        )
        cardHandler.exchangeCommand(apduData1)

        val apduData2 = APDUExchange.Request(
            cla = 0x80,
            lc = 0x02,
            le = 0x00,
            ins = 0xA8,
            p1 = 0x00,
            p2 = 0x00,
            dataIn = byteArrayOf(0x83.toByte(), 0x00)
        )
        cardHandler.exchangeCommand(apduData2)

        val apduData3 =
            APDUExchange.Request(cla = 0x00, le = 0x00, ins = 0xB2, p1 = 0x01, p2 = 0x14)
        val apduResult3: APDUExchange.Response = cardHandler.exchangeCommand(apduData3)

        val stream = ByteArrayInputStream(apduResult3.dataOut)
        return if (apduResult3.dataOutLen != 0) {
            val tlv: BERTLV = TLVUtil.getNextTLV(stream)
            return getCardInfo(tlv.valueBytes)
        } else
            CardInfo("", "")

    }


    private fun getCardInfo(
        data: ByteArray,
        cardNumber: String = "",
        cardExpiryDate: String = ""
    ): CardInfo {
        var number: String = cardNumber
        var date: String = cardExpiryDate
        try {
            val stream = ByteArrayInputStream(data)
            while (stream.available() > 0) {
                val tlv: BERTLV = TLVUtil.getNextTLV(stream)
                val valueBytes: ByteArray = tlv.valueBytes
                val tag: Tag = tlv.tag
                val panTag: Tag = EMVTags.getNotNull(ByteUtils.hexString2ByteArray("5A"))
                val expireTag: Tag = EMVTags.getNotNull(ByteUtils.hexString2ByteArray("5f24"))
                if (tag.tagBytes === panTag.tagBytes) {
                    val value: String =
                        Util.prettyPrintHex(Util.byteArrayToHexString(valueBytes))
                    number = if (value.isNotBlank()) value else continue
                } else if (tag.tagBytes === expireTag.tagBytes) {
                    val value: String =
                        Util.prettyPrintHex(Util.byteArrayToHexString(valueBytes))
                    date = if (value.isNotBlank()) value else continue
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val cleanPan: String = number.replace(" ", "")
        val cleanDate: String = date.replace(" ", "")
        val exDate: String = cleanDate.substring(0, 4)
        val month: String = exDate.substring(2, 4)
        val year: String = exDate.substring(0, 2)
        val expiryDate: String = month + year
        return CardInfo(cleanPan, expiryDate)
    }
}