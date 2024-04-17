package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart.delegate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.ViewholderFeatureUserCashierRefundProductListBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    private val refundChecked: (RefundCart.Product, Boolean) -> Unit,
    private val refundReceiptProduct: (RefundCart.Product) -> Unit
) : RecyclerViewDelegate<RefundCart.Product>(target) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter(refundChecked, refundReceiptProduct)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    private class Adapter(
        private val refundChecked: (RefundCart.Product, Boolean) -> Unit,
        private val refundReceiptProduct: (RefundCart.Product) -> Unit
    ) : RecyclerViewAdapter<RefundCart.Product, Adapter.ViewHolder>() {

        override fun getItemId(item: RefundCart.Product): Long =
            item.uid

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<RefundCart.Product> {

            override fun onBind(element: RefundCart.Product) {
                binding.apply {
                    constraintLayout.isEnabled = element.forRefund
                    linearLayout.setOnClickListener {
                        if (element.forRefund) refundReceiptProduct.invoke(element)
                        else checkBox.performClick()
                    }

                    checkBox.apply {
                        isChecked = element.forRefund
                        setOnClickListener { refundChecked(element, isChecked) }
                    }

                    quantityTextView.apply {
                        text = element.let { element ->
                            element.unit?.let { "${element.quantity.roundToString()} ${it.name}" }
                                ?: element.quantity.roundToString()
                        }
                        visibility = View.VISIBLE
                    }

                    nameTextView.text = element.name
                    priceTextView.setTextAmount(
                        bigDecimal = element.amount,
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                }
            }
        }
    }
}