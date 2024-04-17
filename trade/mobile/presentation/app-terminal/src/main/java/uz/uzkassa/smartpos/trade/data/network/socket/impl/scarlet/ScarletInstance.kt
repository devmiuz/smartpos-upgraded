package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.retry.LinearBackoffStrategy
import okhttp3.OkHttpClient
import okhttp3.Request
import uz.uzkassa.smartpos.trade.BuildConfig
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.adapter.message.KotlinSerializerMessageAdapter
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.adapter.stream.FlowStreamAdapterFactory
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.client.OkHttpStompClient
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.client.OkHttpStompClient.ClientOpenRequest
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.client.OkHttpStompMainChannel.Configuration
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.OkHttpInstance
import java.util.concurrent.TimeUnit

class ScarletInstance(
    private val okHttpInstance: OkHttpInstance,
    private val url: String,
    private val login: String? = null,
    private val password: String? = null
) {
    private val okHttpClient: OkHttpClient by lazy {
        okHttpInstance.getOkHttpClient {
            this.connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .writeTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.MINUTES)
        }
    }

    val protocol: OkHttpStompClient by lazy {
        OkHttpStompClient(
            configuration = Configuration("", false),
            okHttpClient = okHttpClient,
            requestFactory = {
                ClientOpenRequest(Request.Builder().url(url).build(), login, password)
            }
        )
    }

    val scarlet: Scarlet by lazy {
        Scarlet(protocol, scarletConfiguration)
    }

    val scarletConfiguration: Scarlet.Configuration by lazy {
        Scarlet.Configuration(
//            backoffStrategy = LinearBackoffStrategy(300000),
            backoffStrategy = LinearBackoffStrategy(10000),
            messageAdapterFactories = listOf(KotlinSerializerMessageAdapter),
            streamAdapterFactories = listOf(FlowStreamAdapterFactory),
            debug = BuildConfig.DEBUG
        )
    }

    companion object {
        const val APAY_BASE_URL: String = "wss://socket-dev.a-pay.uz/ws"
        const val APAY_LOGIN = "guest"
        const val APAY_PASSWORD = "guest"
        val SMARPOTS_BASE_URL: String =
            (if (BuildConfig.DEBUG) "https://api-dev.smartpos.uz"
            else "https://api.smartpos.uz") + "/websocket/tracker/websocket"
        const val CONNECTION_TIMEOUT_SECONDS: Long = 60
    }
}