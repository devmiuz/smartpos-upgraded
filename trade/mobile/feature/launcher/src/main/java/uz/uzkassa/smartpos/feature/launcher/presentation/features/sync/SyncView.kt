package uz.uzkassa.smartpos.feature.launcher.presentation.features.sync

import moxy.MvpView

internal interface SyncView : MvpView {

    fun onLoadingSyncState()

    fun onErrorSyncState(throwable: Throwable)
}