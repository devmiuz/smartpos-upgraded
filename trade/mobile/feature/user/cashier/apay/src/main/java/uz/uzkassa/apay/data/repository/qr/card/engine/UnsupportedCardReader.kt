package uz.uzkassa.apay.data.repository.qr.card.engine

import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.CardResult
import uz.uzkassa.apay.data.repository.qr.card.CardReader

object UnsupportedCardReader : CardReader {

    override fun startReadingCard(): CardResult<CardInfo> =
        throw UnsupportedOperationException()

    override fun stopReadingCard() =
        throw UnsupportedOperationException()
}