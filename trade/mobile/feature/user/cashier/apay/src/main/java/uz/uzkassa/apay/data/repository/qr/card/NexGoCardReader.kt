package uz.uzkassa.apay.data.repository.qr.card

import android.content.Context
import com.nexgo.oaf.apiv3.APIProxy
import com.nexgo.oaf.apiv3.DeviceEngine
import com.nexgo.oaf.apiv3.card.cpu.CPUCardHandler
import com.nexgo.oaf.apiv3.device.reader.CardSlotTypeEnum
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.CardResult
import uz.uzkassa.apay.data.repository.qr.card.engine.type.nexgo.NexGoCardHandler
import javax.inject.Inject

class NexGoCardReader constructor(context: Context) : CardReader {

    private val deviceEngine: DeviceEngine = APIProxy.getDeviceEngine(context)
    private val cardSlotTypes: Set<CardSlotTypeEnum> =
        setOf(CardSlotTypeEnum.ICC1, CardSlotTypeEnum.RF, CardSlotTypeEnum.SWIPE)

    private val cardReader by lazy { deviceEngine.cardReader }
    private val cpuCardHandler: CPUCardHandler by lazy {
        deviceEngine.getCPUCardHandler(CardSlotTypeEnum.ICC1)
    }
    private val emvHandler by lazy { deviceEngine.getEmvHandler("app1") }
    private val cardScanOperations by lazy {
        NexGoCardReaderHelper(
            cardReader,
            emvHandler,
            cardSlotTypes.toHashSet(),
            NexGoCardHandler(cpuCardHandler)
        )
    }

    override fun startReadingCard(): CardResult<CardInfo> =
        cardScanOperations.startReadingCard()


    override fun stopReadingCard() =
        cardScanOperations.stopReadingCard()
}