package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.delegate

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.EndlessScrollListener
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.view.setThrottledClickListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.product.ProductListResource.Success.Type
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleMainProductEmptyBinding as EmptyBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleMainProductSelectionBinding as SelectionBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.selection.ProductSelectionFragment

internal class RecyclerViewDelegate(
    target: ProductSelectionFragment,
    private val onProductClicked: (Product) -> Unit,
    private val onProductFavoriteClicked: (Product) -> Unit,
    private val onPageChanged: (page: Int) -> Unit
) : RecyclerViewStateEventDelegate<Product>(target) {
    private var previousType: Type? = null

    private val endlessScrollListener: EndlessScrollListener by lazy {
        object : EndlessScrollListener(checkNotNull(view?.layoutManager)) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                onPageChanged(page)
            }
        }
    }

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(onProductClicked, onProductFavoriteClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(context, 1)

    override fun onLoading() {
        if (isEmpty) view?.removeOnScrollListener(endlessScrollListener)
        super.onLoading()
    }

    @Deprecated(message = "hidden", level = DeprecationLevel.HIDDEN)
    override fun onSuccess(collection: Collection<Product>) =
        throw UnsupportedOperationException()

    fun onSuccess(mustClear: Boolean, type: Type, collection: Collection<Product>) {
        (recyclerViewAdapter as? Adapter)?.setToBeenSearched(type == Type.SEARCH)
        if (mustClear) clear()
        if (previousType != type) {
            endlessScrollListener.resetState()
            clear()
            previousType = type
        }
        super.onSuccess(collection)
    }

    override fun onSuccess(collection: Collection<Product>, behavior: ItemChangesBehavior) {
        val list: List<Product> = collection.filterNot { containsInAdapter(it) }
        when {
            isEmpty && list.isNotEmpty() -> view?.addOnScrollListener(endlessScrollListener)
            list.isEmpty() -> view?.removeOnScrollListener(endlessScrollListener)
        }
        super.onSuccess(list, behavior)
    }

    private class Adapter(
        private val onProductClicked: (Product) -> Unit,
        private val onProductFavoriteClicked: (Product) -> Unit
    ) : RecyclerViewAdapter<Product, RecyclerView.ViewHolder>() {

        private var hasBeenSearched: Boolean = false

        fun setToBeenSearched(isSearched: Boolean) {
            hasBeenSearched = isSearched
        }

        override fun getItemViewType(position: Int): Int =
            if (isEmpty) VIEW_TYPE_EMPTY
            else super.getItemViewType(position)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)

            return when (viewType) {
                VIEW_TYPE_EMPTY ->
                    EmptyViewHolder(EmptyBinding.inflate(inflater, parent, false))
                else -> ViewHolder(SelectionBinding.inflate(inflater, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            when (holder) {
                is EmptyViewHolder -> holder.onBind()
                else -> super.onBindViewHolder(holder, position)
            }

        override fun getItemId(item: Product): Long =
            item.id

        override fun update(item: Product) = when {
            hasBeenSearched || item.isFavorite -> super.update(item)
            else -> remove(item)
        }

        inner class EmptyViewHolder(
            private val binding: EmptyBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun onBind() {
                binding.apply {
                    iconImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_star)
                    descriptionTextView.setText(
                        if (!hasBeenSearched)
                            R.string.viewholder_feature_user_cashier_sale_main_product_selection_empty_favorite_products_message
                        else R.string.viewholder_feature_user_cashier_sale_main_product_selection_empty_searched_products_message
                    )
                }
            }
        }

        @Suppress("UNUSED_PARAMETER")
        inner class ViewHolder(
            private val binding: SelectionBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<Product> {

            @SuppressLint("SetTextI18n")
            override fun onBind(element: Product) {
                binding.apply {
                    constraintLayout.setThrottledClickListener(1000) { onProductClicked(element) }
                    productFavoriteStateImageView.apply {
                        setOnClickListener { onProductFavoriteClicked.invoke(element) }
                        isSelected = element.isFavorite
                    }
                    productNameTextView.text = element.name
                    productSalesPriceTextView.setTextAmount(
                        bigDecimal = element.salesPrice,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                }
            }
        }
    }

    private companion object {
        const val VIEW_TYPE_EMPTY: Int = 1
    }
}