package uz.uzkassa.smartpos.feature.category.selection.setup.presentation.delegate

import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.widget.getDrawable
import uz.uzkassa.smartpos.feature.category.common.presentation.CategoryCommonRecyclerViewDelegate
import uz.uzkassa.smartpos.feature.category.selection.setup.R
import uz.uzkassa.smartpos.feature.category.selection.setup.databinding.ViewHolderFeatureCategorySelectionSetupBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    onCategoryClicked: (Category) -> Unit
) : CategoryCommonRecyclerViewDelegate(target, onCategoryClicked) {

    override fun getCategoryTreeViewBinder(): CategoryTreeViewBinder<*> =
        TreeViewBinder()

    internal inner class TreeViewBinder : CategoryTreeViewBinder<TreeViewBinder.ViewHolder>() {

        override fun provideViewHolder(itemView: View): ViewHolder = ViewHolder(itemView)

        override fun getLayoutId(): Int =
            R.layout.view_holder_feature_category_selection_setup

        inner class ViewHolder(itemView: View) : CategoryViewHolder(itemView) {
            private val binding: ViewBinding = ViewBinding.bind(itemView)

            override fun onBindView(category: Category, isExpanded: Boolean) {
                binding.apply {
                    categoryNameTextView.text = category.name
                    checkBox.isChecked = category.isEnabled
                    expandedImageView.apply {
                        setImageDrawable(getExpandedDrawable(isExpanded))
                        visibility =
                            if (category.childCategories.isNotEmpty()) View.VISIBLE
                            else View.INVISIBLE
                    }
                }
            }

            override fun setCheckedChangeListener(action: (Boolean) -> Unit) =
                binding.checkBox.setOnClickListener { action(binding.checkBox.isChecked) }

            override fun onExpandedStateChanged(isExpanded: Boolean) =
                binding.expandedImageView.setImageDrawable(getExpandedDrawable(isExpanded))

            private fun getExpandedDrawable(isExpanded: Boolean): Drawable? =
                getDrawable(
                    if (isExpanded) R.drawable.core_presentation_vector_drawable_chevron_up
                    else R.drawable.core_presentation_vector_drawable_chevron_down
                )?.apply { setTint(itemView.context.colorAccent) }
        }
    }
}