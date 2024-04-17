package uz.uzkassa.smartpos.feature.user.list.presentation.delegate

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.view.setThrottledClickListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.context
import uz.uzkassa.smartpos.core.presentation.utils.widget.getColor
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.user.list.R
import uz.uzkassa.smartpos.feature.user.list.presentation.UserListFragment
import uz.uzkassa.smartpos.feature.user.list.databinding.ViewHolderFeatureUserManageListBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: UserListFragment,
    private val onUserClicked: (User) -> Unit,
    private val onUserDismissalClicked: (User) -> Unit
) : RecyclerViewStateEventDelegate<User>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(onUserClicked, onUserDismissalClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onSuccess(collection: Collection<User>) {
        val result: MutableCollection<User> = arrayListOf()
        for (element: User in collection) {
            if (!containsInAdapter(element))
                result.add(element)
            else
                update(element)
        }
        super.onSuccess(result)
    }

    private class Adapter(
        private val onUserClicked: (User) -> Unit,
        private val onUserDismissalClicked: (User) -> Unit
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
                    if (!element.isBlocked) {
                        menuLinearLayout.setThrottledClickListener {
                            onUserDismissalClicked(element)
                            easySwipeMenuLayout.resetStatus()
                        }

                        constraintLayout.setThrottledClickListener { onUserClicked(element) }
                    } else constraintLayout.also {
                        it.isClickable = false
                        it.isLongClickable = false
                    }

                    easySwipeMenuLayout.isCanLeftSwipe = !element.isBlocked

                    stateImageView.setImageResource(
                        if (!element.isBlocked) R.drawable.core_presentation_vector_drawable_chevron_right
                        else R.drawable.core_presentation_vector_drawable_trash
                    )

                    stateImageView.drawable?.setTint(
                        if (!element.isBlocked) context.colorAccent
                        else getColor(android.R.color.darker_gray)
                    )

                    userNameTextView.setTextColor(
                        if (!element.isBlocked) getColor(android.R.color.black)
                        else getColor(android.R.color.darker_gray)
                    )

                    userNameTextView.text = element.fullName.fullName
                    userRoleTextView.text = element.userRole.nameRu
                    phoneNumberTextView.text = TextUtils.toPhoneNumber(element.phoneNumber)
                }
            }
        }

    }
}