package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.tab.delegate

import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_OPEN
import androidx.fragment.app.Fragment
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.constants.GlobalConstants
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.DrawerLayoutDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.LayoutFeatureUserCashierSaleDrawerLayoutBinding as ViewBinding

internal class DrawerLayoutDelegate(
    fragment: Fragment,
    private val listener: DrawerLayoutDelegateListener
) : DrawerLayoutDelegate(fragment), DrawerListener {
    private var binding: ViewBinding? = null

    override fun onCreate(view: DrawerLayout, childView: View, savedInstanceState: Bundle?) {
        super.onCreate(view, childView, savedInstanceState)
        binding = ViewBinding.bind(childView).apply {
            syncButton.setOnClickListener {
                closeDrawer()
                listener.onSyncClicked()
            }
            notificationsLinearLayout.setOnClickListener {
                closeDrawer()
                listener.onNotificationsClicked()
            }
            settingsLinearLayout.setOnClickListener {
                closeDrawer()
                listener.onSettingsClicked()
            }
            autoPrintButton.setOnClickListener {
                closeDrawer()
                listener.onAutoPrint()
            }
            pauseShiftLinearLayout.setOnClickListener {
                closeDrawer()
                listener.onPauseShiftClicked()
            }

            logoutLinearLayout.setOnClickListener {
                closeDrawer()
                listener.onUserLogoutClicked()
            }
            appVersionTextView.text = context.getString(
                R.string.core_presentation_app_version,
                GlobalConstants.appVersion
            )
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    fun setUser(user: User) {
        lockDrawer(false)
        binding?.apply {
            userNameTextView.text = user.fullName.fullName
            userRoleTextView.text = user.userRole.nameRu
        }
    }

    fun reset() {
        lockDrawer(true)
        binding?.userNameTextView?.text = null
    }

    fun lockDrawer() {
        setLockMode(LOCK_MODE_LOCKED_OPEN)
        binding?.apply {
            syncButton.isEnabled = false
            settingsLinearLayout.isEnabled = false
            pauseShiftLinearLayout.isEnabled = false
        }
    }

    interface DrawerLayoutDelegateListener {

        fun onSyncClicked()

        fun onNotificationsClicked()

        fun onSettingsClicked()

        fun onAutoPrint()

        fun onPauseShiftClicked()

        fun onUserLogoutClicked()
    }
}