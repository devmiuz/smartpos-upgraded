package uz.uzkassa.apay.presentation.features.card

import android.content.Context
import android.util.Log
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.apay.domain.ApayInteractor
import uz.uzkassa.apay.domain.PaymentDataInteractor
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.transaction.TransactionHolder
import javax.inject.Inject

internal class CardApayPresenter @Inject constructor(
    private val cashierApayRouter: CashierApayRouter,
    private val coroutineContextManager: CoroutineContextManager,
    private val apayInteractor: ApayInteractor,
    private val apayQRCodeFeatureArgs: CashierApayFeatureArgs,
    private val cashierApayFeatureCallback: CashierApayFeatureCallback
) : MvpPresenter<CardApayView>() {

    private var billId: String? = null
    private var apayProviderId: Int? = null

    private var context: Context? = null

    private var paymentDataInteractor: PaymentDataInteractor? = null

    private var cardNumber: String? = null
    private var expiry: String? = null

    var disposable = CompositeDisposable()
    private var stompClient: StompClient? = null

    override fun onFirstViewAttach() {
        createApayBillId()
    }

    private fun createApayBillId() {
        apayInteractor.createBill(apayQRCodeFeatureArgs.leftAmount, apayQRCodeFeatureArgs.uniqueId)
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onStartCreateBill()
            }
            .onSuccess {
                with(it) {
                    this@CardApayPresenter.billId = billId
                    this@CardApayPresenter.apayProviderId = providerId
                }
                viewState.onSuccessCreateBill()
            }
            .onFailure {
                viewState.onErrorCreateBill(it)
                cashierApayRouter.exit()
            }
    }

    fun checkPayUzCard() {
        billId?.let {
            apayInteractor.checkPayUzcard(it)
                .launchCatchingIn(presenterScope)
                .onSuccess {
                    if (it.code == "PAID") {
                        cashierApayFeatureCallback.finish(
                            apayInteractor.getPaidAmount()
                                .copy(
                                    transactionHolder = TransactionHolder(
                                        apayProviderId,
                                        billId
                                    )
                                ),
                            billId ?: "none"
                        )
                    } else if (it.code == "CANCELLED") {
                        cancelPay()
                    }
                }.onFailure {
                    cancelPay()
                }
        }
    }

    fun setContext(context: Context) {
        this.context = context
        paymentDataInteractor = PaymentDataInteractor(coroutineContextManager, this.context!!)
    }


    //    fun backCashierQrGeneratorScreen() = cashierApayRouter.backToCashierQrGeneratorScreen()
    fun backCashierQrGeneratorScreen() = cashierApayRouter.exit()

    fun checkCardSlots() {
        paymentDataInteractor!!.getCardSlot()
            .launchCatchingIn(presenterScope)
            .onSuccess {
                if (it) {
                    getInsertedCardInfo()
                } else {
                    viewState.onCardInsertedEvent(it)
                }
            }
    }

    private fun getInsertedCardInfo() {
        paymentDataInteractor!!.isPaymentAllowed()
            .flatMapConcat { isAllowed ->
                paymentDataInteractor!!.getCardInfo(isAllowed)
            }
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onCardInfoLoading() }
            .onSuccess { viewState.onCardInfoDefined(it) }
            .onFailure { viewState.onCardInfoError(it) }
    }


    fun payCard(cardNumber: String, expiry: String) {
        this.cardNumber = cardNumber
        this.expiry = expiry
        billId?.let { billId ->
            apayInteractor.updateBill(billId = billId, pan = cardNumber, expiry = expiry)
                .launchCatchingIn(presenterScope)
                .onStart { viewState.onLoadingPay() }
                .onSuccess {
                    if (it.bilsmsSentlId) {
                        viewState.onSuccessPay(it)
                    } else {
                        viewState.onSmsNotSending()
                    }
                }
                .onFailure {
                    viewState.onErrorPay(it)
                }
        }
    }

    fun payBill(cardId: String? = null, confirmationKey: String? = null) {
        billId?.let {
            apayInteractor.payBill(it, cardId, confirmationKey)
                .launchCatchingIn(presenterScope)
                .onStart {
                    viewState.onLoadingPay()
                }
                .onSuccess { billResponse ->
                    connectWebsocket()
                }
                .onFailure {
                    viewState.onErrorPay(it)
                }
        }
    }

    private fun connectWebsocket() {
        if (stompClient != null && stompClient?.isConnected == true) {
            stompClient?.disconnect()
        }
        val headers: MutableList<StompHeader> = ArrayList()
        headers.add(StompHeader("login", "smartpos_user"))
        headers.add(StompHeader("passcode", "smartpos_user"))
        headers.add(StompHeader("host", "/"))
        headers.add(StompHeader("accept-version", "1.1"))

        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "wss://socket-dev.a-pay.uz/ws"
        ).withClientHeartbeat(10000).withServerHeartbeat(10000)

        stompClient?.lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        startTimer()
                        Log.d("Stomp connection opened", "test")
                    }
                    LifecycleEvent.Type.ERROR -> {
                        //                        apayQRCodeFeatureArgs.socketBroadcastChannel.sendBlocking(SocketData.CLOSE)
                        Log.e("error", "Stomp connection error", lifecycleEvent.exception)
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        //                        apayQRCodeFeatureArgs.socketBroadcastChannel.sendBlocking(SocketData.CLOSE)
                        Log.e("closed", "Stomp connection error", lifecycleEvent.exception)
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        cancelPay()
                        Log.e(
                            "error",
                            "Stomp connection FAILED_SERVER_HEARTBEAT",
                            lifecycleEvent.exception
                        )
                    }
                }
            }?.let {
                disposable.add(
                    it
                )
            }
        //JSONObject(it.payload.substring(1, it.payload.length-1).replace("\\n", "").replace("\\", ""))

        stompClient
            ?.topic("/topic/session-kkm-bill-$billId")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                // todo : chek chiqarish
                Log.wtf("test sss", "$it")
                val jsonObject: JSONObject = if (it.payload.startsWith("\"")) {
                    JSONObject(
                        it.payload.substring(1, it.payload.length - 1).replace("\\n", "")
                            .replace("\\", "")
                    )
                } else {
                    JSONObject(
                        it.payload.replace("\\n", "")
                            .replace("\\", "")
                    )
                }
                when (jsonObject.get("billStatus")) {
                    "PAID" -> {
                        cashierApayFeatureCallback.finish(
                            apayInteractor.getPaidAmount()
                                .copy(
                                    transactionHolder = TransactionHolder(
                                        apayProviderId,
                                        billId
                                    )
                                ),
                            billId ?: "none"
                        )
                    }
                    "CANCELED" -> cancelPay()
                }
            }, {
                Log.wtf("test error sss", it)
                cancelPay()
            })?.let {
                disposable.add(
                    it
                )
            }
        stompClient?.connect(headers)
    }

    fun getCardUi(cardNumber: String) {
        apayInteractor.getCardData(cardNumber)
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onCardDataLoading()
            }
            .onSuccess {
                viewState.onCardDataSuccess(it)
            }
            .onFailure {
                viewState.onCardDataError(it)
            }
    }


    private fun startTimer() {
        apayInteractor
            .startCountdownTimer()
            .onEach {
                Log.d("long", it.toString())
                if (it == ApayInteractor.COUNTDOWN_TIMER_AMOUNT) {
                    stompClient?.disconnect()
                    viewState.openCheckPayDialog()
                } else {
                    val totalSeconds = ApayInteractor.COUNTDOWN_TIMER_AMOUNT - 1;
                    val progress = when (it) {
                        1L -> {
                            100.0
                        }
                        totalSeconds -> {
                            0.0
                        }
                        else -> {
                            100.0 * (ApayInteractor.COUNTDOWN_TIMER_AMOUNT - it).toDouble() / (ApayInteractor.COUNTDOWN_TIMER_AMOUNT - 1).toDouble()
                        }
                    }

                    val time = ApayInteractor.COUNTDOWN_TIMER_AMOUNT - it
//                    if (time < 10) {
//                        viewState.onTick(progress = progress.toInt(), time = "00:0$time")
//                    } else {
//                        viewState.onTick(progress = progress.toInt(), time = "00:$time")
//                    }
                }
            }
            .launchIn(presenterScope)
    }


    fun cancelPay() {
        cashierApayRouter.exit()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (stompClient != null && stompClient?.isConnected == true) {
            stompClient?.disconnect()
        }
    }

}