package uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout

interface DrawerStateListener {

    fun openDrawer()

    fun closeDrawer()

    fun setLockState(enable: Boolean)
}