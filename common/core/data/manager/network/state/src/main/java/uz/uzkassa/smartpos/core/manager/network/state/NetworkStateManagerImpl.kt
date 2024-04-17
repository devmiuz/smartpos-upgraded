@file:Suppress("DEPRECATION")

package uz.uzkassa.smartpos.core.manager.network.state

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.ConnectivityManager.EXTRA_NETWORK_INFO
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import uz.uzkassa.smartpos.core.manager.network.state.listener.NetworkStateListener
import java.lang.ref.WeakReference
import java.util.*
import android.net.ConnectivityManager.NetworkCallback as ConnectionNetworkCallback

internal class NetworkStateManagerImpl(context: Context) : NetworkStateManager {
    private val broadcastReceiver: BroadcastReceiver = getBroadcastReceiver()
    private val contextReference: WeakReference<Context> = WeakReference(context)
    private var networkCallback: NetworkCallback? = null

    private val listeners: MutableSet<NetworkStateListener> = HashSet()
    private val stateMap: MutableMap<String, Boolean> = HashMap()

    private var isConnected: Boolean = false
    private var isNotifying: Boolean = false
    private var isRegistered: Boolean = false

    private val connectivityManager: ConnectivityManager? by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as? ConnectivityManager
    }

    private val context: Context?
        get() = contextReference.get()

    private val isLollipopOrAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    @Suppress("DEPRECATION")
    override fun register() {
        val intentFilter: IntentFilter =
            IntentFilter().apply {
                val action: String =
                    if (!isLollipopOrAbove) CONNECTIVITY_ACTION
                    else NetworkCallback.INTENT_CONNECTIVITY_ACTION_NAME
                addAction(action)
            }

        if (!isRegistered) {
            context?.registerReceiver(broadcastReceiver, intentFilter)
            isRegistered = true

            if (isLollipopOrAbove)
                registerNetworkCallback()
        }
    }

    override fun unregister() {
        if (isRegistered) {
            context?.unregisterReceiver(broadcastReceiver)
            isRegistered = false

            if (isLollipopOrAbove)
                unregisterNetworkCallback()
        }
    }

    override fun addNetworkStateListener(listener: NetworkStateListener) {
        if (!listeners.contains(listener)) {
            stateMap[listener.javaClass.name] = true
            listeners.add(listener)
            notifyListener(listener)
        }
    }

    override fun removeNetworkStateListener(listener: NetworkStateListener) {
        if (listeners.contains(listener)) {
            if (isNotifying) stateMap.remove(listener.javaClass.name)
            else listeners.remove(listener)
        }
    }

    private fun notifyListeners(isConnected: Boolean) {
        if (!isNotifying) {
            isNotifying = true

            val removableListeners: MutableList<NetworkStateListener> = arrayListOf()
            this.isConnected = isConnected

            listeners.asIterable().forEach {
                val isEnabled: Boolean = stateMap[it.javaClass.name] ?: false
                if (isEnabled) notifyListener(it)
                else removableListeners.add(it)
            }

            listeners.removeAll(removableListeners)
        }

        isNotifying = false
    }

    private fun notifyListener(listener: NetworkStateListener) {
        if (isConnected) listener.onNetworkAvailable()
        else listener.onNetworkUnavailable()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun registerNetworkCallback() {
        if (isLollipopOrAbove && networkCallback == null) {
            connectivityManager?.let {
                networkCallback = NetworkCallback(context)
                it.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback!!)
            }
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun unregisterNetworkCallback() {
        if (networkCallback != null) {
            connectivityManager?.let {
                it.unregisterNetworkCallback(networkCallback)
                networkCallback = null
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getBroadcastReceiver(): BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val isConnected: Boolean = when (intent.action) {
                    EXTRA_NETWORK_INFO -> {
                        val networkInfo: NetworkInfo? =
                            connectivityManager?.let {
                                val networkInfo: NetworkInfo? =
                                    intent.getParcelableExtra(EXTRA_NETWORK_INFO)
                                return@let if (networkInfo != null)
                                    it.getNetworkInfo(networkInfo.type)
                                else null
                            }

                        networkInfo != null && networkInfo.isConnected
                    }

                    else -> {
                        val connectionState: NetworkCallback.ConnectionState? =
                            intent.getParcelableExtra(NetworkCallback.INTENT_STATE_EXTRA_NAME)
                        connectionState != null && connectionState.isConnected
                    }
                }

                notifyListeners(isConnected)
            }
        }

    private class NetworkCallback(context: Context?) : ConnectionNetworkCallback() {
        private val contextReference: WeakReference<Context?> = WeakReference(context)

        override fun onAvailable(network: Network) {
            contextReference.get()?.sendBroadcast(getConnectivityIntent(true))
        }

        override fun onLost(network: Network) {
            contextReference.get()?.sendBroadcast(getConnectivityIntent(false))
        }

        override fun onUnavailable() {
            contextReference.get()?.sendBroadcast(getConnectivityIntent(false))
        }

        private fun getConnectivityIntent(isConnected: Boolean): Intent =
            Intent().setAction(INTENT_CONNECTIVITY_ACTION_NAME)
                .putExtra(INTENT_STATE_EXTRA_NAME, ConnectionState(isConnected))

        data class ConnectionState(val isConnected: Boolean) : Parcelable {

            constructor(parcel: Parcel) : this(parcel.readByte() != 0.toByte())

            override fun writeToParcel(parcel: Parcel, flags: Int) =
                parcel.writeByte(if (isConnected) 1 else 0)

            override fun describeContents(): Int = 0

            companion object CREATOR : Parcelable.Creator<ConnectionState> {
                override fun createFromParcel(parcel: Parcel): ConnectionState =
                    ConnectionState(parcel)

                override fun newArray(size: Int): Array<ConnectionState?> = arrayOfNulls(size)
            }
        }

        companion object {
            const val INTENT_CONNECTIVITY_ACTION_NAME: String = "NETWORK_STATE_CONNECTIVITY_CHANGE"
            const val INTENT_STATE_EXTRA_NAME: String = "connectionState"
        }
    }
}