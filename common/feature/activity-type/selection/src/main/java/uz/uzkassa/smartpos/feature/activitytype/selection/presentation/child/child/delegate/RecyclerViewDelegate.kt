package uz.uzkassa.smartpos.feature.activitytype.selection.presentation.child.child.delegate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.databinding.ViewHolderFeatureActivityTypeChildSelectionBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    private val childActivityTypeClickListener: (ChildActivityType) -> Unit
) : RecyclerViewStateEventDelegate<ChildActivityType>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(childActivityTypeClickListener)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.itemAnimator = null
    }

    private class Adapter(
        private val childActivityTypeClickListener: (ChildActivityType) -> Unit
    ) : RecyclerViewAdapter<ChildActivityType, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: ChildActivityType): Long =
            item.activityType.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<ChildActivityType> {

            override fun onBind(element: ChildActivityType) {
                binding.apply {
                    constraintLayout.setOnClickListener {
                        childActivityTypeClickListener
                            .invoke(element.copy(isSelected = !selectionCheckBox.isChecked))
                    }

                    titleTextView.text = element.activityType.name
                    selectionCheckBox.isChecked = element.isSelected
                }
            }
        }
    }
}