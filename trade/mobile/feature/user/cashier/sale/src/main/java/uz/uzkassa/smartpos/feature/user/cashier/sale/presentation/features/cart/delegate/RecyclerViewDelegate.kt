package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.delegate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.utils.view.setThrottledClickListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.math.toStringCompat
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart.ItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleMainShoppingBagBinding as ShoppingBagBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleMainShoppingBagEmptyBinding as ShoppingBagEmptyBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.SaleCartFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.delegate.SalePaymentRecyclerViewDelegate

internal class RecyclerViewDelegate(
    target: SaleCartFragment,
    private val onChangeShoppingBagItemTypeClicked: (ItemType) -> Unit,
    private val onDeleteShoppingBagItemTypeClicked: (ItemType) -> Unit
) : RecyclerViewDelegate<ItemType>(target) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter(onChangeShoppingBagItemTypeClicked, onDeleteShoppingBagItemTypeClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.isMotionEventSplittingEnabled = false
        view.itemAnimator = null
        clear()
    }

    private class Adapter(
        private val onChangeShoppingBagItemTypeClicked: (ItemType) -> Unit,
        private val onDeleteShoppingBagItemTypeClicked: (ItemType) -> Unit
    ) : RecyclerViewAdapter<ItemType, RecyclerView.ViewHolder>() {

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is EmptyViewHolder -> holder.onBind()
                is ViewHolder -> holder.onBind(getItem(position))
                else -> super.onBindViewHolder(holder, position - 1)
            }
        }

        override fun getItemViewType(position: Int): Int =
                if (isEmpty) VIEW_TYPE_EMPTY
            else super.getItemViewType(position)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)

            return when (viewType) {
                VIEW_TYPE_EMPTY ->
                    EmptyViewHolder(ShoppingBagEmptyBinding.inflate(inflater, parent, false))
                else -> ViewHolder(ShoppingBagBinding.inflate(inflater, parent, false))
            }
        }

        override fun getItemId(item: ItemType): Long =
            item.uid

        override fun getItemCount(): Int =
            if (isEmpty) 1 else super.getItemCount()

        override fun add(item: ItemType) =
            if (isEmpty) super.add(item) else add(0, item)

        override fun update(item: ItemType) {
            remove(item)
            add(item)
        }

        class EmptyViewHolder(
            private val binding: ShoppingBagEmptyBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun onBind() {
                binding.descriptionTextView.text =
                    getString(R.string.viewholder_feature_user_cashier_sale_main_shopping_bag_empty_state_title)
            }
        }

        inner class ViewHolder(
            private val binding: ShoppingBagBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<ItemType> {

            override fun onBind(element: ItemType) {
                binding.apply {
                    constraintLayout.setThrottledClickListener(1000) {
                        onChangeShoppingBagItemTypeClicked(element)
                    }

                    menuLinearLayout.setOnClickListener {
                        onDeleteShoppingBagItemTypeClicked.invoke(element)
                    }

                    salesPriceTextView.setTextAmount(
                        bigDecimal = element.amount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )

                    when (element) {
                        is ItemType.FreePrice -> onBindFreePrice()
                        is ItemType.Product -> onBindProductType(element)
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            private fun onBindFreePrice() {
                binding.apply {
                    quantityTextView.visibility = View.GONE
                    nameTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_shopping_bag_item_free_price_title)
                }
            }

            @SuppressLint("SetTextI18n")
            private fun onBindProductType(element: ItemType.Product) {
                binding.apply {
                    quantityTextView.apply {
                        text = element.let { element ->
                            element.unit?.let { "${element.quantity.roundToString()} ${it.name}" }
                                ?: element.quantity.roundToString()
                        }
                        visibility = View.VISIBLE
                    }

                    countAndUnitTextView.apply {
                        text = getString(
                            R.string.core_presentation_common_vat_amount_currency_uzs,
                            element.vatAmount.toStringCompat()
                        )
                    }

                    nameTextView.text = element.name
                }
            }
        }

        companion object {
            const val VIEW_TYPE_EMPTY: Int = 1
        }
    }
}