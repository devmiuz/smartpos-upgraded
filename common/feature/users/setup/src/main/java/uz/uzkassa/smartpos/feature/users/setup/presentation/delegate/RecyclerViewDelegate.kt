package uz.uzkassa.smartpos.feature.users.setup.presentation.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.widget.getColor
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.users.setup.databinding.ViewHolderUsersSetupBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment
) : RecyclerViewStateEventDelegate<User>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter()

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onSuccess(collection: Collection<User>) =
        onSuccess(collection, ItemChangesBehavior.UPSERT)

    private class Adapter : RecyclerViewAdapter<User, Adapter.ViewHolder>() {

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
                    userNameTextView.setTextColor(getColor(android.R.color.black))

                    userNameTextView.text = element.fullName.fullName
                    userRoleTextView.text = element.userRole.nameRu
                    phoneNumberTextView.text = TextUtils.toPhoneNumber(element.phoneNumber)
                }
            }
        }
    }
}