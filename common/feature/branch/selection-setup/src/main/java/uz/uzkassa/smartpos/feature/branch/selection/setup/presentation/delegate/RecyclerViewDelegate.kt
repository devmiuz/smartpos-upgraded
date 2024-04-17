package uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.delegate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.model.BranchSelection
import uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.BranchSelectionSetupFragment
import uz.uzkassa.smartpos.feature.branch.selection.setup.databinding.ViewHolderFeatureBranchSelectionSetupBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: BranchSelectionSetupFragment,
    private val branchClicked: (branch: Branch, isSelected: Boolean) -> Unit
) : RecyclerViewStateEventDelegate<BranchSelection>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(branchClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.itemAnimator = null
    }

    private class Adapter(
        private val branchClicked: (branch: Branch, isSelected: Boolean) -> Unit
    ) : RecyclerViewAdapter<BranchSelection, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: BranchSelection): Long =
            item.branch.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root), ViewHolderItemBinder<BranchSelection> {

            override fun onBind(element: BranchSelection) {
                binding.apply {
                    constraintLayout.setOnClickListener {
                        branchClicked.invoke(element.branch, !radioButton.isChecked)
                    }
                    radioButton.setOnClickListener {
                        branchClicked.invoke(element.branch, radioButton.isChecked)
                    }
                    radioButton.isChecked = element.isSelected
                    branchNameTextView.text = element.branch.name
                    branchAddressTextView.text = element.branch.address
                }
            }
        }
    }
}