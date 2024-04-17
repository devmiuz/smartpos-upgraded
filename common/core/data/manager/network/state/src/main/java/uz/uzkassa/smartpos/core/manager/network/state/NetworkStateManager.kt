package uz.uzkassa.smartpos.core.manager.network.state

import android.content.Context
import uz.uzkassa.smartpos.core.manager.network.state.listener.NetworkStateListener

interface NetworkStateManager {

    fun register()

    fun unregister()

    fun addNetworkStateListener(listener: NetworkStateListener)

    fun removeNetworkStateListener(listener: NetworkStateListener)

    companion object {

        fun instantiate(context: Context): NetworkStateManager =
            NetworkStateManagerImpl(context)
    }
}