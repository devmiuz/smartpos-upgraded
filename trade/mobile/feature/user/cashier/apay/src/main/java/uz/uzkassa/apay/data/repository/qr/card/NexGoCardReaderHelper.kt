package uz.uzkassa.apay.data.repository.qr.card

import com.nexgo.oaf.apiv3.SdkResult
import com.nexgo.oaf.apiv3.device.reader.CardInfoEntity
import com.nexgo.oaf.apiv3.device.reader.CardSlotTypeEnum
import com.nexgo.oaf.apiv3.device.reader.OnCardInfoListener
import com.nexgo.oaf.apiv3.emv.*
import uz.uzkassa.apay.data.model.CardInfo
import uz.uzkassa.apay.data.model.CardResult
import uz.uzkassa.apay.data.repository.qr.card.engine.AsyncRunnable
import uz.uzkassa.apay.data.repository.qr.card.engine.CardHandler
import uz.uzkassa.apay.data.repository.qr.card.engine.CardReadingProcess
import uz.uzkassa.launcher.data.manager.sam.card.model.CardError
import java.util.concurrent.atomic.AtomicReference
import com.nexgo.oaf.apiv3.device.reader.CardReader as NexGoCardReader

class NexGoCardReaderHelper (
    private val cardReader: NexGoCardReader,
    private val emvHandler: EmvHandler,
    private val cardSlotTypes: HashSet<CardSlotTypeEnum>,
    private val cardHandler: CardHandler
) : CardReader {

    private val cardReadingProcess by lazy { CardReadingProcess(cardHandler) }

    override fun startReadingCard(): CardResult<CardInfo> {
        return AsyncRunnable.wait(
            object : AsyncRunnable<CardResult<CardInfo>>() {
                override fun run(notifier: AtomicReference<CardResult<CardInfo>>) {
                    cardReader.searchCard(
                        cardSlotTypes,
                        20,
                        object : OnCardInfoListener {

                            override fun onCardInfo(p0: Int, p1: CardInfoEntity?) {
                                if (p0 == SdkResult.Success && p1 != null) {
                                    val emvBalanceResult: CardResult<CardInfo> =
                                        getEmvBalance(EmvChannelTypeEnum.FROM_ICC, emvHandler)
                                    finish(notifier, emvBalanceResult)
                                } else finish(notifier, CardResult.error(CardError.CONFIRM_CARD))
                            }

                            override fun onSwipeIncorrect() {
                                finish(notifier, CardResult.error(CardError.SWIPE_INCORRECT))
                            }

                            override fun onMultipleCards() {
                                finish(notifier, CardResult.error(CardError.MULTIPLE_CARDS))
                            }
                        }
                    )
                }
            }
        )
    }

    private fun getEmvBalance(
        emvChannelTypeEnum: EmvChannelTypeEnum,
        emvHandler: EmvHandler
    ): CardResult<CardInfo> {
        return AsyncRunnable.wait(
            object : AsyncRunnable<CardResult<CardInfo>>() {
                override fun run(notifier: AtomicReference<CardResult<CardInfo>>) {
                    emvHandler.emvGetEcBalance(
                        emvChannelTypeEnum,
                        object : OnEmvProcessListener {

                            override fun onCertVerify(p0: String?, p1: String?) {
                                emvHandler.onSetCertVerifyResponse(true)
                                finish(notifier, CardResult.error(CardError.CONFIRM_CERT))
                            }

                            override fun onFinish(p0: Int, p1: EmvProcessResultEntity?) {
                                finish(
                                    notifier,
                                    CardResult.success(cardReadingProcess.getCardInfo())
                                )
                            }

                            override fun onRequestAmount() {
                                emvHandler.onSetRequestAmountResponse(null)
                                finish(notifier, CardResult.error(CardError.AMOUNT_DIGITS))
                            }

                            override fun onConfirmCardNo(p0: CardInfoEntity?) {
                                emvHandler.onSetAfterFinalSelectedAppResponse(true)
                                finish(notifier, CardResult.error(CardError.CONFIRM_CARD))
                            }

                            override fun onAfterFinalSelectedApp() {
                                finish(notifier, CardResult.error(CardError.SELECTED_APP))
                            }

                            override fun onSelApp(
                                p0: MutableList<String>?,
                                p1: MutableList<CandidateAppInfoEntity>?,
                                p2: Boolean
                            ) {
                                finish(notifier, CardResult.error(CardError.APP_LIST))
                            }

                            override fun onPrompt(p0: PromptEnum?) {
                                finish(notifier, CardResult.error(CardError.PROMPT_INFORMATION))
                            }

                            override fun onReadCardAgain() {
                                finish(notifier, CardResult.error(CardError.READ_CARD_AGAIN))
                            }

                            override fun onConfirmEcSwitch() {
                                finish(notifier, CardResult.error(CardError.CONFIRM_E_CASH))
                            }

                            override fun onRemoveCard() {
                                finish(notifier, CardResult.error(CardError.CARD_REMOVED))
                            }

                            override fun onCardHolderInputPin(p0: Boolean, p1: Int) {
                                finish(notifier, CardResult.error(CardError.CARD_HOLDER_INPUT_PIN))
                            }

                            override fun onOnlineProc() {
                                finish(notifier, CardResult.error(CardError.ONLINE_PROC))
                            }
                        }
                    )
                }
            }
        )
    }

    override fun stopReadingCard() {
        emvHandler.clearLog()
        cardReader.stopSearch()
        cardHandler.powerOff()
    }
}