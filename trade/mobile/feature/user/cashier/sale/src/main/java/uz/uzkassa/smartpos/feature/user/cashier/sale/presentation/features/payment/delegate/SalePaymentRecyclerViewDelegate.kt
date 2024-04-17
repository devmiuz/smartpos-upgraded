package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.delegate

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.utils.content.getAmountSpan
import uz.uzkassa.smartpos.core.presentation.utils.widget.context
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.SalePayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.discount.SaleDiscount
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.SalePaymentFragment
import java.math.BigDecimal
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSalePaymentDetailsPaymentAmountBinding as ViewBinding

internal class SalePaymentRecyclerViewDelegate(
    target: SalePaymentFragment
) : RecyclerViewDelegate<Any>(target) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter()

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    fun setSalePayment(salePayment: SalePayment) =
        (recyclerViewAdapter as Adapter).setSalePayment(salePayment)

    private class Adapter : RecyclerViewAdapter<Any, RecyclerView.ViewHolder>() {
        private var saleDiscount: SaleDiscount? = null

        override fun getItemViewType(position: Int): Int = when {
            position == 0 -> VIEW_TYPE_PAYMENT_DISCOUNT_ARBITRARY
            isEmpty -> VIEW_TYPE_EMPTY
            else -> super.getItemViewType(position - 1)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {

            val binding: ViewBinding =
                ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return when (viewType) {
                VIEW_TYPE_PAYMENT_DISCOUNT_ARBITRARY -> DiscountViewHolder(binding)
                VIEW_TYPE_EMPTY -> EmptyStateViewHolder(binding)
                else -> ViewHolder(binding)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is EmptyStateViewHolder -> holder.onBind()
                is DiscountViewHolder -> holder.onBind(saleDiscount)
                else -> super.onBindViewHolder(holder, position - 1)
            }
        }

        override fun getItemId(item: Any): Long =
            item.hashCode().toLong()

        override fun getItemCount(): Int =
            super.getItemCount() + 1

        fun setSalePayment(salePayment: SalePayment) {
            saleDiscount = salePayment.saleDiscount
            set(salePayment.receiptPayments)
        }

        class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<ReceiptPayment> {

            override fun onBind(element: ReceiptPayment) {
                binding.apply {
                    nameTextView.text = when (element.type) {
                        Type.CARD ->
                            getString(R.string.core_presentation_common_payment_type_card)
                        Type.CASH ->
                            getString(R.string.core_presentation_common_payment_type_cash)
                        Type.OTHER ->
                            getString(R.string.core_presentation_common_payment_type_other)
                        else -> element.type.name
                    }

                    amountTextView.setTextAmount(
                        element.amount,
                        R.string.core_presentation_common_amount_currency_uzs
                    )
                }
            }
        }

        class DiscountViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<SaleDiscount?> {

            override fun onBind(element: SaleDiscount?) {
                binding.apply {
                    nameTextView.text =
                        getString(R.string.viewholder_feature_user_cashier_sale_payment_details_payment_discount_title)

                    amountTextView.text =
                        getString(
                            R.string.viewholder_feature_user_process_cashier_payment_details_payment_discount_value,
                            context.getAmountSpan(
                                bigDecimal = element?.getOrCalculateDiscountAmount ?: BigDecimal.ZERO,
                                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                            ),
                            (element?.getOrCalculateDiscountPercent ?: 0.0).roundToString()
                        )
                }
            }
        }

        class EmptyStateViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun onBind() {
                binding.apply {
                    nameTextView.text =
                        getString(R.string.viewholder_feature_user_cashier_sale_payment_details_payment_payment_title)

                    amountTextView
                        .setTextAmount(
                            BigDecimal.ZERO,
                            R.string.core_presentation_common_amount_currency_uzs
                        )
                }
            }
        }

        private companion object {
            const val VIEW_TYPE_EMPTY: Int = 1
            const val VIEW_TYPE_PAYMENT_DISCOUNT_ARBITRARY: Int = 2
        }
    }
}