package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.delegate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.widget.getColor
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.product.unit.creation.R
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.databinding.ViewHolderFragmentFeatureProductUnitCreationBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    private val topClicked: (ProductUnitDetails) -> Unit,
    private val bottomClicked: (ProductUnitDetails) -> Unit,
    private val unitDeleteClicked: (ProductUnitDetails) -> Unit
) : RecyclerViewStateEventDelegate<ProductUnitDetails>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(topClicked, bottomClicked, unitDeleteClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    private class Adapter(
        private val topArrowClicked: (ProductUnitDetails) -> Unit,
        private val bottomArrowClicked: (ProductUnitDetails) -> Unit,
        private val unitDeleteClicked: (ProductUnitDetails) -> Unit
    ) : RecyclerViewAdapter<ProductUnitDetails, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: ProductUnitDetails): Long = item.unit.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<ProductUnitDetails> {

            @SuppressLint("SetTextI18n")
            override fun onBind(element: ProductUnitDetails) {
                binding.apply {
                    topArrowImageView.setOnClickListener { topArrowClicked.invoke(element) }
                    bottomArrowImageView.setOnClickListener { bottomArrowClicked.invoke(element) }
                    deleteUnitImageView.setOnClickListener { unitDeleteClicked.invoke(element) }
                    if (element.isBase) {
                        viewHolderCardView.apply {
                            setCardBackgroundColor(getColor(R.color.product_unit_creation_view_holder_primary_color))
                            viewHolderCardView.radius = resources.getDimension(R.dimen._8sdp)
                        }
                        deleteUnitImageView.isVisible = false
                        unitOrderTextView.text = (absoluteAdapterPosition + 1).toString() + "."
                        unitDetailsTextView.text = getString(
                            R.string.fragment_feature_product_unit_creation_product_units_first_item,
                            element.unit.name
                        )
                    } else {
                        viewHolderCardView.apply {
                            setCardBackgroundColor(getColor(R.color.design_default_color_background))
                            viewHolderCardView.radius = resources.getDimension(R.dimen._8sdp)
                        }
                        deleteUnitImageView.isVisible = true
                        unitOrderTextView.text = (absoluteAdapterPosition + 1).toString() + "."
                        unitDetailsTextView.text =
                            "1  ${element.unit.name} =  ${element.count.toInt()} ${element.parentUnit?.name}"
                    }
                }
            }
        }
    }
}
