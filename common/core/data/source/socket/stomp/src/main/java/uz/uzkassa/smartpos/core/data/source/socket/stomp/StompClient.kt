package uz.uzkassa.smartpos.core.data.source.socket.stomp

import okhttp3.OkHttpClient
import uz.uzkassa.smartpos.core.data.source.socket.stomp.config.StompConfig
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.StompSession
import uz.uzkassa.smartpos.core.data.source.socket.stomp.session.StompSessionImpl
import java.util.*
import org.hildan.krossbow.stomp.StompClient as KStompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient as OkHttp

class StompClient(okHttpClient: OkHttpClient, config: StompConfig = StompConfig()) {
    private val stompClient = KStompClient(OkHttp(okHttpClient), StompConfig.map(config))
    private val sessions: MutableMap<String, StompSession> = WeakHashMap()

    fun instance(url: String, login: String?, password: String?): StompSession =
        with(sessions) {
            if (!containsKey(url)) put(url, StompSessionImpl(stompClient, url, login, password)) else this[url]
        } ?: throw IllegalStateException("Connection with $url url does not instantiated")

    suspend fun disconnect() {
        val iterator = sessions.iterator()
        while (iterator.hasNext()) {
            val entry: Map.Entry<String, StompSession?> = iterator.next()
            entry.value?.disconnect()
            iterator.remove()
        }
    }
}