package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.delegate.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.SpacesItemDecoration
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.databinding.ViewHolderFeatureLauncherUserAuthCashierBinding

internal class CashierUserRecyclerViewDelegate(
    target: Fragment,
    private val userClickListener: (User) -> Unit
) : RecyclerViewDelegate<User>(target) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter(userClickListener)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(SpacesItemDecoration(R.dimen._1sdp, R.dimen._8sdp))

    private class Adapter(
        private val userClickListener: (User) -> Unit
    ) : RecyclerViewAdapter<User, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                ViewHolderFeatureLauncherUserAuthCashierBinding
                    .inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
            )

        override fun getItemId(item: User): Long =
            item.id

        inner class ViewHolder(
            private val binding: ViewHolderFeatureLauncherUserAuthCashierBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<User> {

            override fun onBind(element: User) {
                binding.apply {
                    linearLayout.setOnClickListener { userClickListener.invoke(element) }
                    userNameTextView.text = element.fullName.firstName
                    userRoleTextView.text = getActualUserRoleName(element.userRole)
                }
            }

            @SuppressLint("DefaultLocale")
            private fun getActualUserRoleName(userRole: UserRole): String =
                when (userRole.type == UserRole.Type.CASHIER) {
                    true -> userRole.nameRu
                    false -> getString(
                        R.string.fragment_feature_launcher_user_auth_owner_login_as_title,
                        getString(R.string.core_presentation_common_user_role_cashier).toLowerCase()
                    )
                }
        }
    }
}