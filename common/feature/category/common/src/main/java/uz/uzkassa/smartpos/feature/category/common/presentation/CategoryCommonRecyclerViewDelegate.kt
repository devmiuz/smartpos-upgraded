package uz.uzkassa.smartpos.feature.category.common.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.tree.RecyclerViewTreeDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.tree.LayoutItemType
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.tree.TreeNode
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.tree.TreeViewBinder
import uz.uzkassa.smartpos.core.utils.collections.replace
import kotlin.properties.Delegates

abstract class CategoryCommonRecyclerViewDelegate(
    target: Fragment,
    private val onCategoryClicked: (Category) -> Unit
) : RecyclerViewTreeDelegate(target) {
    private var treeViewBinder: TreeViewBinder<*> by Delegates.notNull()

    final override fun getTreeViewBinders(): Array<TreeViewBinder<*>> =
        arrayOf(treeViewBinder)

    final override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    final override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        treeViewBinder = getCategoryTreeViewBinder()
    }

    override fun onViewHolderBind(node: TreeNode<*>?, holder: RecyclerView.ViewHolder) =
        (holder as CategoryViewHolder).let {
            it.setCheckedChangeListener { isChecked: Boolean ->
                val category: Category = it.update(node, isChecked)
                recyclerViewAdapter.notifyDataSetChanged()
                onCategoryClicked.invoke(category)
            }
        }

    override fun onViewHolderToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) =
        (holder as CategoryViewHolder).onExpandedStateChanged(isExpand)

    fun set(categories: List<Category>) =
        addAll(map(categories))

    @Suppress("LABEL_NAME_CLASH")
    private fun map(categories: List<Category>): List<TreeNode<*>> =
        categories.map { CategoryItemType(it, treeViewBinder.layoutId) }
            .map { itemType ->
                val childNodes: List<TreeNode<*>> = map(itemType.category.childCategories)
                val node: TreeNode<*> = TreeNode(itemType)
                childNodes.forEach { node.addChild(it) }
                return@map node
            }

    protected abstract fun getCategoryTreeViewBinder(): CategoryTreeViewBinder<*>

    data class CategoryItemType(var category: Category, val layout: Int) : LayoutItemType {
        override fun getLayoutId(): Int = layout
    }

    abstract class CategoryTreeViewBinder<ViewHolder : CategoryViewHolder> :
        TreeViewBinder<ViewHolder>() {

        @Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST")
        override fun bindView(holder: ViewHolder, position: Int, node: TreeNode<*>) {
            (node as? TreeNode<CategoryItemType>)?.let { holder.onBindView(it) }
        }
    }

    abstract class CategoryViewHolder(itemView: View) : TreeViewBinder.ViewHolder(itemView) {

        fun onBindView(node: TreeNode<CategoryItemType>) =
            onBindView(node.content.category, node.isExpand)

        @Suppress("UNCHECKED_CAST")
        fun update(node: TreeNode<*>?, isCategoryChecked: Boolean): Category {
            node as TreeNode<CategoryItemType>
            val element: CategoryItemType = node.content as CategoryItemType

            element.category = element.category.copy(isEnabled = isCategoryChecked)
            node.content = element

            val result: Category = element.category.copy(
                childCategories = updateChildNodes(
                    parentNode = node,
                    isCategoryChecked = isCategoryChecked
                )
            )

            return updateParentNodes(node, result, isCategoryChecked)
        }

        private fun updateParentNodes(
            node: TreeNode<*>?,
            category: Category,
            isCategoryChecked: Boolean
        ): Category {
            val element: CategoryItemType? = (node?.parent?.content as? CategoryItemType)
            return if (element != null) {
                val childCategories: List<Category> =
                    element.category.let { it ->
                        it.childCategories.toMutableList().apply {
                            it.childCategories.find { it.id == category.id }
                                ?.let { replace(it, category) }
                        }
                    }

                val isEnabled: Boolean =
                    childCategories.find { it.isEnabled }?.isEnabled ?: isCategoryChecked

                element.category = element.category.copy(
                    isEnabled = isEnabled,
                    childCategories = childCategories
                )

                updateParentNodes(node.parent, element.category, isCategoryChecked)
            } else category
        }

        @Suppress("UNCHECKED_CAST")
        private fun updateChildNodes(
            parentNode: TreeNode<*>?,
            isCategoryChecked: Boolean
        ): List<Category> {
            val list: MutableList<Category> = arrayListOf()
            parentNode as TreeNode<CategoryItemType>
            parentNode.childList.forEach { it ->
                val childList: List<Category> = updateChildNodes(it, isCategoryChecked)
                val categoryItemType: CategoryItemType =
                    (it as TreeNode<CategoryItemType>).content.let {
                        it.copy(
                            category = it.category.copy(
                                isEnabled = isCategoryChecked,
                                childCategories = childList
                            )
                        )
                    }

                it.content = categoryItemType
                list.add(categoryItemType.category)
            }

            return list
        }

        abstract fun onBindView(category: Category, isExpanded: Boolean)

        abstract fun onExpandedStateChanged(isExpanded: Boolean)

        abstract fun setCheckedChangeListener(action: (Boolean) -> Unit)
    }
}