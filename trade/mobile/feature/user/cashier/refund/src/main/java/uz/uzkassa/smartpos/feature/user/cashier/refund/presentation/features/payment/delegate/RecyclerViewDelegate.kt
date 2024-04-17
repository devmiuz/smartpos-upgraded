package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount.RefundReceiptPayment
import java.math.BigDecimal
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.ViewholderFeatureUserCashierRefundReceiptPaymentBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment
) : RecyclerViewDelegate<Any>(target) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter()

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun set(collection: Collection<Any>) {
        if (collection.isNotEmpty()) super.set(collection)
    }

    private class Adapter : RecyclerViewAdapter<Any, RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int =
            if (isEmpty) VIEW_TYPE_EMPTY
            else super.getItemViewType(position)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val binding = ViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return when (viewType) {
                VIEW_TYPE_EMPTY -> EmptyStateViewHolder(binding)
                else -> ViewHolder(binding)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is EmptyStateViewHolder -> holder.onBind()
                else -> super.onBindViewHolder(holder, position)
            }
        }

        override fun getItemId(item: Any): Long =
            item.hashCode().toLong()

        override fun getItemCount(): Int =
            if (isEmpty) 1 else super.getItemCount()

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<RefundReceiptPayment> {

            override fun onBind(element: RefundReceiptPayment) {
                binding.apply {
                    nameTextView.text = when (element.type) {
                        AmountType.CARD ->
                            getString(R.string.core_presentation_common_payment_type_card)
                        AmountType.CASH ->
                            getString(R.string.core_presentation_common_payment_type_cash)
                    }

                    amountTextView.setTextAmount(
                        bigDecimal = element.amount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                }
            }
        }

        inner class EmptyStateViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun onBind() {
                binding.apply {
                    nameTextView.text =
                        getString(R.string.viewholder_feature_user_cashier_refund_receipt_payment_title)

                    amountTextView
                        .setTextAmount(
                            bigDecimal = BigDecimal.ZERO,
                            currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                        )
                }
            }
        }

        private companion object {
            const val VIEW_TYPE_EMPTY: Int = 1
        }
    }
}