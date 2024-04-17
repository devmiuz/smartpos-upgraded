package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.utils.view.setThrottledClickListener
import uz.uzkassa.smartpos.core.presentation.utils.widget.getString
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.core.utils.primitives.addWhitespace
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleMainCreditAdvanceListBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.CreditAdvanceListFragment
import java.math.BigDecimal

internal class RecyclerViewDelegate(
    target: CreditAdvanceListFragment,
    private val onClicked: (Receipt) -> Unit
) : RecyclerViewStateEventDelegate<Receipt>(target) {

    override fun getItemsAdapter(): RecyclerViewAdapter<Receipt, *> =
        Adapter(onClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onLoading() {
        clear()
        super.onLoading()
    }

    override fun onSuccess(collection: Collection<Receipt>) =
        super.onSuccess(collection, ItemChangesBehavior.UPSERT)

    private class Adapter(
        private val onClicked: (Receipt) -> Unit
    ) : RecyclerViewAdapter<Receipt, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                ViewHolderFeatureUserCashierSaleMainCreditAdvanceListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

        override fun getItemId(item: Receipt): Long {
            return if (item.uid.length == 22) {
                item.uid.substring(12).toLong()
            } else {
                item.uid.substring(11).toLong()
            }
        }

        inner class ViewHolder(
            private val binding: ViewHolderFeatureUserCashierSaleMainCreditAdvanceListBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<Receipt> {

            override fun onBind(element: Receipt) {
                binding.apply {

//                    creditAdvanceItemContainer.setBackgroundColor(
//                        if (element.isFullyPaid) "5743A047".toInt(
//                            radix = 16
//                        ) else Color.WHITE
//                    )

                    if (element.customerName !== null && element.customerName!!.isNotEmpty()) {
                        customerNameContainer.visibility = View.VISIBLE
                        customerNameTextView.text = element.customerName ?: "-"
                    } else {
                        customerNameContainer.visibility = View.GONE
                        customerNameTextView.text = ""
                    }

                    if (element.customerContact !== null && element.customerContact!!.isNotEmpty()) {
                        customerPhoneContainer.visibility = View.VISIBLE
                        customerPhoneTextView.text = element.customerContact ?: "-"
                    } else {
                        customerPhoneContainer.visibility = View.GONE
                        customerPhoneTextView.text = ""
                    }

                    nameTextView.text = getString(
                        R.string.viewholder_feature_user_cashier_sale_main_receipt_draft_external_name,
                        checkNotNull(element.receiptDate).toString("HH:mm dd.MM.yyyy")
                    )

                    paidAmountTextView.text = element.totalPaid.addWhitespace()

                    val totalCost: BigDecimal =
                        element.receiptDetails
                            .map { it.amount }.sum()
                            .subtract(element.totalDiscount)

                    totalPaymentAmountTextView.text = "/${totalCost.addWhitespace()}"

                    receiptTypeTextView.text = element.baseStatus.name

                    creditAdvanceItemContainer.setThrottledClickListener { onClicked(element) }

                }
            }
        }
    }
}