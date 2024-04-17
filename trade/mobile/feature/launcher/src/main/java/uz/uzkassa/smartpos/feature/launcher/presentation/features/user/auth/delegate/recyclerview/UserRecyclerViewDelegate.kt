package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.delegate.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.launcher.databinding.ViewHolderFeatureLauncherUserSupervisorBinding as ViewBinding

internal class UserRecyclerViewDelegate(
    fragment: Fragment,
    private val userClickListener: (User) -> Unit
) : RecyclerViewDelegate<User>(fragment) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter { userClickListener.invoke(it) }

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    private class Adapter(
        private val userClickListener: (User) -> Unit
    ) : RecyclerViewAdapter<User, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: User): Long =
            item.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<User> {

            override fun onBind(element: User) {
                binding.apply {
                    when (element.userRole.type) {
                        UserRole.Type.BRANCH_ADMIN, UserRole.Type.OWNER ->
                            constraintLayout.setOnClickListener { userClickListener.invoke(element) }
                        else -> {
                            constraintLayout.isEnabled = false
                            inDevelopmentTextView.isVisible = true
                        }
                    }

                    userNameTextView.text = element.fullName.firstName
                    userRoleTextView.text = element.userRole.nameRu
                }
            }
        }
    }
}