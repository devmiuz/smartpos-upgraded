package uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout

class DrawerStateDelegate : DrawerStateListener {
    private var listener: DrawerStateListener? = null

    var isOpen: Boolean = false
        private set

    fun setStateListener(listener: DrawerStateListener) {
        if (this.listener == null)
            this.listener = listener
    }

    override fun openDrawer() {
        listener?.openDrawer()
        isOpen = true
    }

    override fun closeDrawer() {
        listener?.closeDrawer()
        isOpen = false
    }

    fun toggle() =
        if (isOpen) closeDrawer() else openDrawer()

    override fun setLockState(enable: Boolean) {
        listener?.setLockState(enable)
    }
}