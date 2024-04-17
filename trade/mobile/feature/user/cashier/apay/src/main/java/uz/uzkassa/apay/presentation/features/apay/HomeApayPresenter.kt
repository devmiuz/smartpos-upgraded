package uz.uzkassa.apay.presentation.features.apay

import android.util.Log
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import uz.uzkassa.apay.data.model.ApayBillDetail
import uz.uzkassa.apay.data.model.ClientData
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.apay.domain.ApayInteractor
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.BadRequestHttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.transaction.TransactionHolder
import javax.inject.Inject

internal class HomeApayPresenter @Inject constructor(
    private val cashierApayRouter: CashierApayRouter,
    private val coroutineContextManager: CoroutineContextManager,
    private val apayInteractor: ApayInteractor,
    private val apayQRCodeFeatureArgs: CashierApayFeatureArgs,
    private val cashierApayFeatureCallback: CashierApayFeatureCallback,
    private val flow: Lazy<Flow<ClientData>>
) : MvpPresenter<HomeApayView>() {

    private var apayBillDetail: ApayBillDetail? = null
    private var cardUid: String? = null
    var disposable = CompositeDisposable()
    private var stompClient: StompClient? = null


    override fun onFirstViewAttach() {
        createApayBillId()
        listenClientId()
    }

    private fun listenClientId() {
        flow.get()
            .onEach {
                updateBill(it.clientId)
            }.launchIn(presenterScope)
    }

    private fun updateBill(clientId: String) {
        apayBillDetail?.let {
            apayInteractor.updateBill(
                billId = it.billId,
                clientId = clientId
            )
                .launchCatchingIn(presenterScope)
                .onStart { viewState.onLoadingUpdateBill() }
                .onSuccess {
                    viewState.onSuccessUpdateBill(it)
                    connectWebsocket()
                }
                .onFailure {
                    viewState.onFailureUpdateBill(it)
                    exit()
                }
        }
    }

    private fun createApayBillId() {
        apayInteractor.createBill(apayQRCodeFeatureArgs.leftAmount, apayQRCodeFeatureArgs.uniqueId)
            .launchCatchingIn(presenterScope)
            .onStart {
                viewState.onStartCreateBill()
            }
            .onSuccess {
                apayBillDetail = it
                cashierApayRouter.apayBillDetail = it
                viewState.onSuccessCreateBill()
            }
            .onFailure {
                viewState.onErrorCreateBill(it)
                cashierApayRouter.exit()
            }
    }

    fun payNfcCard(nfc: String) {
        cardUid = nfc
        apayBillDetail?.let { apayBillDetail ->
            apayInteractor.updateBill(billId = apayBillDetail.billId, nfc = nfc)
                .launchCatchingIn(presenterScope)
                .onStart { viewState.onLoadingPay() }
                .onSuccess {
                    if (it.tryPin) {
                        viewState.onSuccessPay()
                    } else {
                        viewState.onNotFound()
                    }
                }
                .onFailure {
                    viewState.onErrorPay(it)
                }
        }
    }

    fun cardList(pin: String) {
        apayBillDetail?.let { apayBillDetail ->
            apayInteractor.cardList(apayBillDetail.billId, cardUid ?: "", pin)
                .launchCatchingIn(presenterScope)
                .onStart {
                    viewState.onLoadingPay()
                }
                .onSuccess {
                    viewState.showCardList(it)
                }
                .onFailure {
                    viewState.onErrorPay(it)
                }
        }
    }

    fun payBill(cardId: String? = null) {
        apayBillDetail?.let { apayBillDetail ->
            apayInteractor.payBill(apayBillDetail.billId, cardId)
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
                        Log.e("error", "Stomp connection error", lifecycleEvent.exception)
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        Log.e("closed", "Stomp connection error", lifecycleEvent.exception)
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
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
            ?.topic("/topic/session-kkm-bill-$apayBillDetail")
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
                            apayInteractor.getPaidAmount().copy(
                                transactionHolder = TransactionHolder(
                                    apayBillDetail?.providerId,
                                    apayBillDetail?.billId
                                )
                            ),
                            apayBillDetail?.billId ?: "none"
                        )
                    }
                    "CANCELED" -> exit()
                }
            }, {
                Log.wtf("test error sss", it)
                exit()
            })?.let {
                disposable.add(
                    it
                )
            }
        stompClient?.connect(headers)
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

    fun checkPaying() {
        apayBillDetail?.let {
            apayInteractor.checkPayUzcard(it.billId)
                .launchCatchingIn(presenterScope)
                .onSuccess {
                    if (it.code == "PAID") {
                        cashierApayFeatureCallback.finish(
                            apayInteractor.getPaidAmount().copy(
                                transactionHolder = TransactionHolder(
                                    apayBillDetail?.providerId,
                                    apayBillDetail?.billId
                                )
                            ),
                            apayBillDetail?.billId ?: "none"
                        )
                    } else if (it.code == "CANCELLED") {
                        exit()
                    }
                }.onFailure {
                    exit()
                }
        }
    }

    fun openQrGenerateScreen() = cashierApayRouter.openQrGeneratorScreen()

    fun openQrScannerScreen() = cashierApayRouter.openQrScannerScreen()

    fun openPhoneScreen() = cashierApayRouter.openPhoneScreen()

    fun exit() = cashierApayRouter.exit()

    override fun onDestroy() {
        super.onDestroy()
        if (stompClient != null && stompClient?.isConnected == true) {
            stompClient?.disconnect()
        }
    }
}