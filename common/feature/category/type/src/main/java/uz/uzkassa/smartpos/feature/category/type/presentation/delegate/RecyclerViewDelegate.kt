package uz.uzkassa.smartpos.feature.category.type.presentation.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.category.type.R
import uz.uzkassa.smartpos.feature.category.type.data.model.CategoryType
import uz.uzkassa.smartpos.feature.category.type.presentation.CategoryTypeFragment
import uz.uzkassa.smartpos.feature.category.type.databinding.ViewHolderFeatureCategoryTypeBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: CategoryTypeFragment,
    private val categoryTypeClicked: (CategoryType) -> Unit
) : RecyclerViewStateEventDelegate<CategoryType>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(categoryTypeClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    private class Adapter(
        private val categoryTypeClicked: (CategoryType) -> Unit
    ) : RecyclerViewAdapter<CategoryType, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: CategoryType): Long =
            item.ordinal.toLong()

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<CategoryType> {

            override fun onBind(element: CategoryType) {
                binding.apply {
                    linearLayout.setOnClickListener { categoryTypeClicked.invoke(element) }

                    nameTextView.text = when (element) {
                        CategoryType.LIST ->
                            getString(R.string.view_holder_feature_category_type_list_title)
                        CategoryType.MAIN ->
                            getString(R.string.view_holder_feature_category_type_main_title)
                    }
                }
            }
        }
    }
}