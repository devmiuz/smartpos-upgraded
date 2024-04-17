package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.delegate

import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.constants.GlobalConstants
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.DrawerLayoutDelegate
import uz.uzkassa.smartpos.feature.supervisior.dashboard.databinding.LayoutSupervisorDirectionDrawerlayoutBinding as ViewBinding

internal class DrawerLayoutDelegate(
    fragment: Fragment,
    private val listener: DrawerLayoutDelegateListener
) : DrawerLayoutDelegate(fragment), DrawerLayout.DrawerListener {
    private var binding: ViewBinding? = null

    override fun onCreate(view: DrawerLayout, childView: View, savedInstanceState: Bundle?) {
        super.onCreate(view, childView, savedInstanceState)
        binding = ViewBinding.bind(childView).apply {
            syncButton.setOnClickListener {
                closeDrawer()
                listener.onSyncClicked()
            }

            receiptCheckLayout.setOnClickListener { listener.onReceiptCheckClicked() }

            branchesLinearLayout.setOnClickListener { listener.onBranchListClicked() }
            usersLinearLayout.setOnClickListener { listener.onUserListClicked() }
            productManageLinearLayout.setOnClickListener { listener.onProductManageClicked() }
            notificationsLinearLayout.setOnClickListener { listener.onNotificationsClicked() }
            settingsLinearLayout.setOnClickListener { listener.onSettingsClicked() }
            logoutLinearLayout.setOnClickListener { listener.onUserLogoutClicked() }
            appVersionTextView.text = context.getString(
                R.string.core_presentation_app_version,
                GlobalConstants.appVersion
            )
        }
    }

    override fun onResume() =
        lockDrawer(false)

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    fun onSuccessUserAuth(user: User) {
        lockDrawer(false)
        binding?.apply {
            userNameTextView.text = user.fullName.fullName
            userRoleTextView.text = user.userRole.nameRu
        }
    }

    fun onResetUserAuth() {
        lockDrawer(true)
        binding?.userNameTextView?.text = null
    }

    interface DrawerLayoutDelegateListener {

        fun onReceiptCheckClicked()

        fun onBranchListClicked()

        fun onUserListClicked()

        fun onProductManageClicked()

        fun onNotificationsClicked()

        fun onSettingsClicked()

        fun onSyncClicked()

        fun onUserLogoutClicked()
    }
}