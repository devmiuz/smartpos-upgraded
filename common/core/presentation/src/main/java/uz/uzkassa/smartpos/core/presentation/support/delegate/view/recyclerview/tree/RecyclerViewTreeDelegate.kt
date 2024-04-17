package uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.tree

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.tree.TreeNode
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.tree.TreeViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.tree.TreeViewBinder

abstract class RecyclerViewTreeDelegate(
    target: Any?,
    lifecycleOwner: LifecycleOwner?
) : ViewDelegate<RecyclerView>(target, lifecycleOwner), TreeViewAdapter.OnTreeNodeListener {

    constructor(lifecycleOwner: LifecycleOwner?) : this(null, lifecycleOwner)

    private val adapter: TreeViewAdapter?
        get() = view?.adapter as? TreeViewAdapter

    protected open val recyclerViewAdapter: TreeViewAdapter
        get() = adapter ?: throw IllegalStateException("Adapter is not defined in delegate")

    protected val isEmpty: Boolean
        get() = adapter?.itemCount == 0

    protected val isNotEmpty: Boolean
        get() = !isEmpty

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.setHasFixedSize(true)

        view.also {
            if (it.layoutManager == null)
                it.layoutManager = getLayoutManager()
        }

        getItemDecoration()?.forEach { view.addItemDecoration(it) }
    }

    override fun onDestroy() {
        if (view?.adapter != null) view?.adapter = null
        super.onDestroy()
    }

    protected open fun addAll(treeNodes: List<TreeNode<*>>) {
        val adapter = TreeViewAdapter(treeNodes, getTreeViewBinders().toList())
        adapter.setOnTreeNodeListener(this)
        view?.adapter = adapter
    }

    final override fun onBind(node: TreeNode<*>?, holder: RecyclerView.ViewHolder) =
        onViewHolderBind(node, holder)

    final override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
        if (!node.isLeaf) {
            onToggle(!node.isExpand, holder)
            if (!node.isExpand)
                adapter?.collapseBrotherNode(node)
        }
        onViewHolderClick(node, holder)
        return false
    }

    final override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) =
        onViewHolderToggle(isExpand, holder)

    protected open fun onViewHolderBind(
        node: TreeNode<*>?,
        holder: RecyclerView.ViewHolder
    ) {
    }

    protected open fun onViewHolderClick(
        node: TreeNode<*>,
        holder: RecyclerView.ViewHolder
    ) {
    }

    protected open fun onViewHolderToggle(
        isExpand: Boolean,
        holder: RecyclerView.ViewHolder
    ) {
    }


    protected open fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? = null

    protected abstract fun getLayoutManager(): RecyclerView.LayoutManager

    protected abstract fun getTreeViewBinders(): Array<TreeViewBinder<*>>
}