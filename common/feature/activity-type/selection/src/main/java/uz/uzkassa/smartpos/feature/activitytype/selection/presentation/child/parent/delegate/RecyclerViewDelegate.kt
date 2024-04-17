package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.parent.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.activitytype.selection.databinding.ViewHolderFeatureActivityTypeParentSelectionBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    private val activityTypeClickListener: (ActivityType) -> Unit
) : RecyclerViewStateEventDelegate<ActivityType>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(activityTypeClickListener)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    private class Adapter(
        private val activityTypeClickListener: (ActivityType) -> Unit
    ) : RecyclerViewAdapter<ActivityType, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: ActivityType): Long =
            item.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<ActivityType> {

            override fun onBind(element: ActivityType) {
                binding.apply {
                    constraintLayout.setOnClickListener {
                        activityTypeClickListener.invoke(element)
                    }

                    nameTextView.text = element.name
                }
            }
        }
    }
}