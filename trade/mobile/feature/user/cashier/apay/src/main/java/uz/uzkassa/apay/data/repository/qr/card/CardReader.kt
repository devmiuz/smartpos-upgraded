package uz.uzkassa.apay.data.repository.qr.card

import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.CardResult

interface CardReader {

    fun startReadingCard(): CardResult<CardInfo>

    fun stopReadingCard()
}