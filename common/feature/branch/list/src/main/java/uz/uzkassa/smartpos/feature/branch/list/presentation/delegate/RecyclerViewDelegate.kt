package uz.uzkassa.smartpos.feature.branch.list.presentation.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.branch.list.data.model.branch.BranchWrapper
import uz.uzkassa.smartpos.feature.branch.list.presentation.BranchListFragment
import uz.uzkassa.smartpos.feature.branch.manage.databinding.ViewHolderFeatureBranchListBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: BranchListFragment,
    private val onBranchClicked: (Branch) -> Unit,
    private val onBranchDeleteClicked: (Branch) -> Unit
) : RecyclerViewStateEventDelegate<BranchWrapper>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(onBranchClicked, onBranchDeleteClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    private class Adapter(
        private val onBranchClicked: (Branch) -> Unit,
        private val onBranchDeleteClicked: (Branch) -> Unit
    ) : RecyclerViewAdapter<BranchWrapper, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: BranchWrapper): Long =
            item.branch.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<BranchWrapper> {

            override fun onBind(element: BranchWrapper) {
                binding.apply {
                    if (!element.branch.isFiscal) {
                        menuLinearLayout.setOnClickListener {
                            onBranchDeleteClicked(element.branch)
                            easySwipeMenuLayout.resetStatus()
                        }
                    }

                    constraintLayout.setOnClickListener { onBranchClicked(element.branch) }

                    easySwipeMenuLayout.isCanLeftSwipe =
                        !element.isCurrent || !element.branch.isFiscal

                    branchNameTextView.text = element.branch.name
                    branchAddressTextView.text = element.branch.address
                }
            }
        }
    }
}