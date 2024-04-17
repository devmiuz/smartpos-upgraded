package uz.uzkassa.smartpos.core.manager.network.state.listener

interface NetworkStateListener {

    fun onNetworkAvailable()

    fun onNetworkUnavailable() {
    }
}