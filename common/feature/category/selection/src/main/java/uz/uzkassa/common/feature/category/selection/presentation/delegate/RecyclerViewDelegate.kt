package uz.uzkassa.common.feature.category.selection.presentation.delegate

import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import uz.uzkassa.common.feature.category.selection.R
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.widget.getDrawable
import uz.uzkassa.smartpos.feature.category.common.presentation.CategoryCommonRecyclerViewDelegate
import uz.uzkassa.common.feature.category.selection.databinding.ViewHolderFeatureCategorySelectionBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    onCategoryClicked: (Category) -> Unit,
    private val onCategoryCheckClicked: (Category) -> Unit

) : CategoryCommonRecyclerViewDelegate(target, onCategoryClicked) {

    override fun getCategoryTreeViewBinder(): CategoryTreeViewBinder<*> =
        TreeViewBinder(onCategoryCheckClicked)

    private class TreeViewBinder(private val onCategoryCheckClicked: (Category) -> Unit) :
        CategoryCommonRecyclerViewDelegate.CategoryTreeViewBinder<TreeViewBinder.ViewHolder>() {

        override fun provideViewHolder(itemView: View): ViewHolder =
            ViewHolder(itemView)

        override fun getLayoutId(): Int =
            R.layout.view_holder_feature_category_selection

        inner class ViewHolder(itemView: View) :
            CategoryCommonRecyclerViewDelegate.CategoryViewHolder(itemView) {
            private val binding: ViewBinding = ViewBinding.bind(itemView)

            override fun onBindView(category: Category, isExpanded: Boolean) {
                binding.apply {
                    categoryNameTextView.apply {
                        text = category.name
                    }
                    categorySelectionCheck.setOnClickListener {
                        onCategoryCheckClicked.invoke(category)
                    }

                    expandedImageView.apply {
                        setImageDrawable(getDrawable(isExpanded))
                        visibility =
                            if (category.childCategories.isNotEmpty()) View.VISIBLE
                            else View.INVISIBLE
                    }
                }
            }

            override fun onExpandedStateChanged(isExpanded: Boolean) =
                binding.expandedImageView.setImageDrawable(getDrawable(isExpanded))

            override fun setCheckedChangeListener(action: (Boolean) -> Unit) = Unit

            private fun getDrawable(isExpanded: Boolean): Drawable? =
                getDrawable(
                    if (isExpanded) R.drawable.core_presentation_vector_drawable_chevron_up
                    else R.drawable.core_presentation_vector_drawable_chevron_down
                )?.apply { setTint(itemView.context.colorAccent) }
        }
    }
}