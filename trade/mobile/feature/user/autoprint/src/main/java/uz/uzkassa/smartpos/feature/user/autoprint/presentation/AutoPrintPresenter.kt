package uz.uzkassa.smartpos.feature.user.autoprint.presentation

import uz.uzkassa.smartpos.feature.user.autoprint.data.models.RemoteReceiptResponse
import android.util.Log
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecodingException
import kotlinx.serialization.parse
import moxy.MvpPresenter
import moxy.presenterScope
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import uz.uzkassa.smartpos.core.data.manager.printer.exception.PrinterException
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.autoprint.data.repository.AutoPrintRepository
import uz.uzkassa.smartpos.feature.user.autoprint.data.repository.params.RemoteReceiptParams
import uz.uzkassa.smartpos.feature.user.autoprint.data.mapper.mapToReceiptDetail
import uz.uzkassa.smartpos.feature.user.autoprint.dependencies.AutoPrintFeatureCallback
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.SamModuleBrokenException
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.SynchronizationException
import java.math.BigDecimal
import javax.inject.Inject

internal class AutoPrintPresenter @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val autoPrintFeatureCallback: AutoPrintFeatureCallback,
    private val autoPrintRepository: AutoPrintRepository
) : MvpPresenter<AutoPrintView>() {


    override fun onFirstViewAttach() {
        connectWebsocket()
    }

    private var stompClient: StompClient? = null
    var disposable = CompositeDisposable()

    @OptIn(ImplicitReflectionSerializer::class, UnstableDefault::class, FlowPreview::class)
    fun connectWebsocket() {
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

        stompClient
            ?.topic("/topic/session-smartpos-receipt-${autoPrintRepository.getDeviceSerialNumber()}")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                // todo : chek chiqarish
                Log.wtf("test sss", "$it")
                val str = it.payload.substring(1, it.payload.length - 1).replace("\\n", "")
                    .replace("\\", "")

                val remoteReceipt = Json.parse<RemoteReceiptResponse>(str)
                println(remoteReceipt)

                val remoteReceiptParams = RemoteReceiptParams(
                    receiptDraftId = null,
                    receiptUid = remoteReceipt.receiptUid,
                    terminalModel = remoteReceipt.terminalModel.toString(),
                    terminalSerialNumber = remoteReceipt.terminalSerialNumber.toString(),
                    totalCard = remoteReceipt.totalCard ?: BigDecimal.ZERO,
                    totalCash = remoteReceipt.totalCash ?: BigDecimal.ZERO,
                    totalCost = remoteReceipt.totalCost ?: BigDecimal.ZERO,
                    totalPaid = remoteReceipt.totalPaid ?: BigDecimal.ZERO,
                    receiptStatus = ReceiptStatus.valueOf(remoteReceipt.status.toString()),
                    receiptDetails = remoteReceipt.remoteReceiptDetails?.map { detail ->
                        detail.mapToReceiptDetail()
                    } ?: listOf(),
                    readonly = remoteReceipt.readOnly,
                    forceToPrint = remoteReceipt.forceToPrint
                )

                createReceipt(remoteReceiptParams)
                    .launchCatchingIn(presenterScope)
                    .onStart {
                        viewState.onReceiptReceived()
                        Log.e("Create: ", "start")
                    }.onSuccess {
                        Log.e("Create: ", "remoter receipt on success")
                        backToRootScreen()
                    }.onFailure {
                        viewState.onError(it)
                        Log.e("Create: ", "failure")
                    }
            }, {
                Log.wtf("test error sss", it)
            })?.let {
                disposable.add(
                    it
                )
            }
        stompClient?.connect(headers)
    }

    @FlowPreview
    fun createReceipt(remoteReceiptParams: RemoteReceiptParams): Flow<Result<Unit>> {
        return autoPrintRepository
            .createReceipt(remoteReceiptParams)
            .catch {
                if (it is PrinterException) {
                    throw it
                }
                if (it is JsonDecodingException) throw SynchronizationException()
                if (it is SamModuleBrokenException) throw SamModuleBrokenException()
                else throw it
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (stompClient != null && stompClient?.isConnected == true) {
            stompClient?.disconnect()
        }
    }

    fun backToRootScreen() =
        autoPrintFeatureCallback.onBack()
}