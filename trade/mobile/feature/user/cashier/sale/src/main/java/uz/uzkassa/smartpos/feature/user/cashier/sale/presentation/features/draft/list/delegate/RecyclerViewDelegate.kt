package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.delegate

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.view.setThrottledClickListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.ReceiptDraftListFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleMainReceiptDraftListBinding as ViewBinding
// #5743A047

internal class RecyclerViewDelegate(
    target: ReceiptDraftListFragment,
    private val onClicked: (ReceiptDraft) -> Unit,
    private val onDeleteClicked: (ReceiptDraft) -> Unit
) : RecyclerViewStateEventDelegate<ReceiptDraft>(target) {

    override fun getItemsAdapter(): RecyclerViewAdapter<ReceiptDraft, *> =
        Adapter(onClicked, onDeleteClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onLoading() {
        clear()
        super.onLoading()
    }

    override fun onSuccess(collection: Collection<ReceiptDraft>) =
        super.onSuccess(collection, ItemChangesBehavior.UPSERT)

    private class Adapter(
        private val onClicked: (ReceiptDraft) -> Unit,
        private val onDeleteClicked: (ReceiptDraft) -> Unit
    ) : RecyclerViewAdapter<ReceiptDraft, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: ReceiptDraft): Long =
            item.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<ReceiptDraft> {

            override fun onBind(element: ReceiptDraft) {
                binding.apply {
                    menuLinearLayout.setOnClickListener { onDeleteClicked(element) }
                    frameLayout.setBackgroundColor(if (element.isRemote) "5743A047".toInt(radix = 16) else Color.WHITE)
                    frameLayout.setThrottledClickListener { onClicked(element) }
                    if (element.receipt.customerName !== null || element.receipt.customerContact !== null) {
                        itemsCustomer.visibility = View.VISIBLE
                        itemsCustomerName.text = element.receipt.customerName ?: "-"
                        itemsCustomerContact.text = element.receipt.customerContact ?: "-"
                    } else {
                        itemsCustomer.visibility = View.GONE
                        itemsCustomerName.text = ""
                        itemsCustomerContact.text = ""
                    }
//                    externalBackgroundView.isVisible = element.isRemote
//                    constraintLayout.setThrottledClickListener { onClicked(element) }
                    nameTextView.text =
                        if (!element.isRemote) element.name
                        else getString(
                            R.string.viewholder_feature_user_cashier_sale_main_receipt_draft_external_name,
                            checkNotNull(element.receipt.receiptDate).toString("HH:mm dd.MM.yyyy")
                        )

                    paymentAmountTextView.setTextAmount(
                        bigDecimal = element.let { it.receipt.totalCost - it.receipt.totalDiscount },
                        currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
                    )
                    itemsCountTextView.text = getString(
                        R.string.viewholder_feature_user_cashier_sale_main_receipt_draft_items_count_value,
                        element.receipt.receiptDetails.size
                    )
                }
            }
        }
    }
}