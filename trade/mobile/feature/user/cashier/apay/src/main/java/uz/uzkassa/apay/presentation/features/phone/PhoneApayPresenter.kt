package uz.uzkassa.apay.presentation.features.phone

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.apay.domain.ApayInteractor
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.transaction.TransactionHolder
import javax.inject.Inject

internal class PhoneApayPresenter @Inject constructor(
    private val cashierApayRouter: CashierApayRouter,
    private val apayInteractor: ApayInteractor,
    private val cashierApayFeatureArgs: CashierApayFeatureArgs,
    private val cashierApayFeatureCallback: CashierApayFeatureCallback
) : MvpPresenter<PhoneApayView>() {

    var disposable = CompositeDisposable()
    private var stompClient: StompClient? = null

    fun backCashierQrGeneratorScreen() = cashierApayRouter.backToHomeApayScreen()

    fun setPhoneNumber(phoneNumber: String) {
        cashierApayRouter.apayBillDetail?.let { apayBillDetail ->
            apayInteractor.updateBill(
                billId = apayBillDetail.billId,
                phone = TextUtils.replaceAllLetters(phoneNumber)
            )
                .launchCatchingIn(presenterScope)
                .onStart { viewState.onLoading() }
                .onSuccess {
                    viewState.onUpdateBillSuccess()
                    connectWebsocket()
                }
                .onFailure {
                    viewState.onError(it)
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
            ?.topic("/topic/session-kkm-bill-${cashierApayRouter.apayBillDetail}")
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
                                    cashierApayRouter.apayBillDetail?.providerId,
                                    cashierApayRouter.apayBillDetail?.billId
                                )
                            ),
                            cashierApayRouter.apayBillDetail?.billId ?: "none"
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

    fun cancelPay() {
        cashierApayRouter.exit()
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
                    if (time < 10) {
                        viewState.onTick(progress = progress.toInt(), time = "00:0$time")
                    } else {
                        viewState.onTick(progress = progress.toInt(), time = "00:$time")
                    }
                }
            }
            .launchIn(presenterScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (stompClient != null && stompClient?.isConnected == true) {
            stompClient?.disconnect()
        }
    }

    fun checkPaying() {
        cashierApayRouter.apayBillDetail?.let {
            apayInteractor.checkPayUzcard(it.billId)
                .launchCatchingIn(presenterScope)
                .onSuccess {
                    if (it.code == "PAID") {
                        cashierApayFeatureCallback.finish(
                            apayInteractor.getPaidAmount().copy(
                                transactionHolder = TransactionHolder(
                                    cashierApayRouter.apayBillDetail?.providerId,
                                    cashierApayRouter.apayBillDetail?.billId
                                )
                            ),
                            cashierApayRouter.apayBillDetail?.billId ?: "none"
                        )
                    } else if (it.code == "CANCELLED") {
                        cancelPay()
                    }
                }.onFailure {
                    cancelPay()
                }
        }
    }

}