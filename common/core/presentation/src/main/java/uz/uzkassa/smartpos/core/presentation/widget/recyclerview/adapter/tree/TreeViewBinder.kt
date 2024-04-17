package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.tree

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

abstract class TreeViewBinder<VH : RecyclerView.ViewHolder> : LayoutItemType {
    abstract fun provideViewHolder(itemView: View): VH

    abstract fun bindView(holder: VH, position: Int, node: TreeNode<*>)

    open class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        @Suppress("UNCHECKED_CAST")
        protected fun <T : View> findViewById(@IdRes id: Int): T {
            return itemView.findViewById<View>(id) as T
        }
    }
}