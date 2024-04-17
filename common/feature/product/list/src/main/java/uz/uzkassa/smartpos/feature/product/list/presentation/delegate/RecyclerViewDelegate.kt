package uz.uzkassa.smartpos.feature.product.list.presentation.delegate

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination.ProductPagination
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.text.DecimalInputFilter
import uz.uzkassa.smartpos.core.presentation.utils.text.RegexInputFilter
import uz.uzkassa.smartpos.core.presentation.utils.text.RegexOutfitFilter
import uz.uzkassa.smartpos.core.presentation.utils.widget.addInputFilter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.product.list.databinding.ViewHolderFeatureProductPaginationBinding as PaginationViewBinding
import uz.uzkassa.smartpos.feature.product.list.presentation.ProductListFragment
import java.math.BigDecimal
import java.math.RoundingMode
import uz.uzkassa.smartpos.feature.product.list.databinding.ViewHolderFeatureProductListBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: ProductListFragment,
    private val onClicked: (product: Product, price: BigDecimal) -> Unit,
    private val onPriceChanged: (product: Product, price: BigDecimal) -> Unit,
    private val onPageChanged: (page: Int) -> Unit
) : RecyclerViewStateEventDelegate<Product>(target) {

    @Suppress("UNCHECKED_CAST")
    override val recyclerViewAdapter: RecyclerViewAdapter<Product, *>
        get() = super.recyclerViewAdapter as RecyclerViewAdapter<Product, *>

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(onClicked, onPriceChanged, onPageChanged)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    @Suppress("UNCHECKED_CAST")
    override fun upsertAll(collection: Collection<Product>) {
        for (element: Product in collection) {
            val list: List<Product> = recyclerViewAdapter.list
            if (list.isNotEmpty() && list.find { it.category?.id == element.category?.id } == null)
                super.remove(element)
            else {
                if (!containsInAdapter(element)) super.add(element)
                else super.update(element)
            }
        }
    }

    override fun onLoading() {
        clear()
        super.onLoading()
    }

    fun onSuccess(productPagination: ProductPagination) {
        (recyclerViewAdapter as Adapter).productPagination = productPagination

        val products: List<Product> = productPagination.products
        val result: MutableCollection<Product> = arrayListOf()
        for (element: Product in products) {
            if (!containsInAdapter(element)) result.add(element)
            else update(element)
        }
        super.onSuccess(products)
    }

    @Deprecated(message = "unused", level = DeprecationLevel.HIDDEN)
    override fun onSuccess(collection: Collection<Product>) =
        throw UnsupportedOperationException()

    private class Adapter(
        private val productClicked: (product: Product, price: BigDecimal) -> Unit,
        private val productPriceChanged: (product: Product, price: BigDecimal) -> Unit,
        private val onPageChanged: (page: Int) -> Unit
    ) : RecyclerViewAdapter<Any, RecyclerView.ViewHolder>() {
        private val priceMap: MutableMap<Long, BigDecimal> = hashMapOf()

        var productPagination: ProductPagination? = null
            set(value) {
                field = value?.copy(products = emptyList())
            }

        override fun getItemViewType(position: Int): Int {
            val showFooter: Boolean =
                productPagination?.let { !(it.isFirst && it.isLast) } ?: false

            return when (isNotEmpty) {
                list.size == position + 1 && showFooter -> VIEW_TYPE_FOOTER
                else -> super.getItemViewType(position)
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                VIEW_TYPE_FOOTER ->
                    FooterViewHolder(PaginationViewBinding.inflate(inflater, parent, false))
                else -> ViewHolder(ViewBinding.inflate(inflater, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is FooterViewHolder -> productPagination?.let { holder.onBind(it) }
                else -> super.onBindViewHolder(holder, position)
            }
        }

        override fun getItemId(item: Any): Long =
            when (item) {
                is Product -> item.id
                else -> (item as ProductPagination).hashCode().toLong()
            }

        private companion object {
            const val VIEW_TYPE_FOOTER: Int = Int.MIN_VALUE
        }

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root), ViewHolderItemBinder<Product> {

            override fun onBind(element: Product) {
                with(binding) {
                    productNameTextView.text = element.name
                    linearLayout.setOnClickListener { productClicked(element, element.salesPrice) }
                    val actualPrice: BigDecimal = priceMap[element.id] ?: element.salesPrice

                    return@with with(productPriceTextInputEditText) {
                        addInputFilter(RegexInputFilter("^(?![.])(^\\d*\\.?\\d*\$)"))
                        addInputFilter(DecimalInputFilter(2))

                        setText(
                            RegexOutfitFilter.removeZeros(actualPrice.toString())
                        )


                        tag = addTextChangedListener { editable ->
                            val bigDecimal: BigDecimal =
                                if (!editable.isNullOrEmpty()) {
                                    BigDecimal(editable.toString()).setScale(
                                        2,
                                        RoundingMode.HALF_EVEN
                                    )
                                } else {
                                    BigDecimal.ZERO
                                }
                            productPriceChanged(element, bigDecimal)
                        }

                    }
                }
            }

            override fun onUnbind() {
                with(binding.productPriceTextInputEditText) {
                    removeTextChangedListener(tag as? TextWatcher)
                }
            }
        }

        inner class FooterViewHolder(
            private val binding: PaginationViewBinding
        ) : RecyclerView.ViewHolder(binding.root), ViewHolderItemBinder<ProductPagination> {

            override fun onBind(element: ProductPagination) {
                with(binding) {
                    with(previousPageLayout) {
                        isInvisible = element.isFirst
                        setOnClickListener { onPageChanged(element.currentPage - 1) }
                    }
                    with(nextPageLayout) {
                        isInvisible = element.isLast
                        setOnClickListener { onPageChanged(element.currentPage + 1) }
                    }
                }
            }
        }
    }
}