package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.delegate.drawerlayout

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.constants.GlobalConstants
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.DrawerLayoutDelegate
import uz.uzkassa.smartpos.feature.launcher.data.model.user.UsersAuth
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.delegate.recyclerview.UserRecyclerViewDelegate
import uz.uzkassa.smartpos.feature.launcher.databinding.LayoutFeatureLauncherUserAuthDrawerlayoutBinding as ViewBinding

internal class DrawerLayoutDelegate(
    fragment: Fragment,
    private val listener: DrawerLayoutDelegateListener,
    private val userClickListener: (User) -> Unit
) : DrawerLayoutDelegate(fragment) {
    private var binding: ViewBinding? = null
    private val recyclerViewDelegate by lazy {
        UserRecyclerViewDelegate(
            fragment = fragment,
            userClickListener = { closeDrawer(); userClickListener.invoke(it) }
        )
    }

    override fun onCreate(view: DrawerLayout, childView: View, savedInstanceState: Bundle?) {
        super.onCreate(view, childView, savedInstanceState)
        binding = ViewBinding.bind(childView).apply {
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            logoutLinearLayout.apply {
                isVisible = true
                setOnClickListener {
                    closeDrawer()
                    listener.onAccountLogout()
                }

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

    fun onSuccessUserAuth(usersAuth: UsersAuth) {
        lockDrawer(false)
        binding?.apply {
            branchNameTextView.text = usersAuth.branch.name
            ownerNameTextView.text = usersAuth.company.name
        }

        recyclerViewDelegate.apply {
            clear();
            addAll(usersAuth.users)
        }
    }

    fun onResetUserAuth() {
        lockDrawer(true)
        binding?.apply {
            branchNameTextView.text = null
            ownerNameTextView.text = null
        }
    }

    interface DrawerLayoutDelegateListener {

        fun onAccountLogout()
    }
}