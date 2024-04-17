package uz.uzkassa.apay.data.repository.qr.card

import android.content.Context
import uz.uzkassa.apay.data.repository.qr.card.device.DeviceManager
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.CardResult
import uz.uzkassa.apay.data.repository.qr.card.engine.UnsupportedCardReader
import javax.inject.Inject

internal class CardReaderManager constructor(context: Context, deviceManager: DeviceManager) : CardReader {

    private val currentCardReader: CardReader = when (deviceManager.deviceInfo.deviceModel) {
        "N5" -> NexGoCardReader(context)
        else -> UnsupportedCardReader
    }

    override fun startReadingCard(): CardResult<CardInfo> =
        currentCardReader.startReadingCard()


    override fun stopReadingCard() =
        currentCardReader.stopReadingCard()
}