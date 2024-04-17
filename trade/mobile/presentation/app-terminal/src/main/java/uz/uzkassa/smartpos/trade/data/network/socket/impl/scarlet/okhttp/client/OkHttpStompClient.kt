/*
 * © 2018 Match Group, LLC.
 */
package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.client

import com.tinder.scarlet.Channel
import com.tinder.scarlet.Protocol
import com.tinder.scarlet.ProtocolSpecificEventAdapter
import com.tinder.scarlet.utils.SimpleProtocolOpenRequestFactory
import com.tinder.scarlet.websocket.okhttp.WebSocketFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.core.IdGenerator
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.generator.UuidGenerator

/**
 * Scarlet protocol implementation for create StompMainChannel
 * @see OkHttpStompMainChannel
 */
class OkHttpStompClient(
    private val configuration: OkHttpStompMainChannel.Configuration,
    private val okHttpClient: OkHttpClient,
    private val requestFactory: (Channel) -> ClientOpenRequest,
    private val idGenerator: IdGenerator = UuidGenerator()
) : Protocol {

    override fun createChannelFactory() = OkHttpStompMainChannel.Factory(
        idGenerator = idGenerator,
        configuration = configuration,
        webSocketFactory = object : WebSocketFactory {
            override fun createWebSocket(request: Request, listener: WebSocketListener) {
                okHttpClient.newWebSocket(request, listener)
            }
        }
    )

    override fun createOpenRequestFactory(
        channel: Channel
    ) = SimpleProtocolOpenRequestFactory {
        requestFactory.invoke(channel)
    }

    override fun createEventAdapterFactory(): ProtocolSpecificEventAdapter.Factory {
        return object : ProtocolSpecificEventAdapter.Factory {}
    }

    data class ClientOpenRequest(
        val okHttpRequest: Request,
        val login: String? = null,
        val passcode: String? = null
    ) : Protocol.OpenRequest
}