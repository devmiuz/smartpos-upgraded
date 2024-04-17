package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.providers.PaymentProvider
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.provider.PaymentProvidersFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSalePaymentProviderBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: PaymentProvidersFragment,
    private val onClicked: (PaymentProvider) -> Unit
) : RecyclerViewStateEventDelegate<PaymentProvider>(target) {

    override fun getItemsAdapter(): RecyclerViewAdapter<PaymentProvider, *> =
        Adapter(onClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onLoading() {
        clear()
        super.onLoading()
    }

    override fun onSuccess(collection: Collection<PaymentProvider>) =
        super.onSuccess(collection, ItemChangesBehavior.UPSERT)

    private class Adapter(
        private val onClicked: (PaymentProvider) -> Unit
    ) : RecyclerViewAdapter<PaymentProvider, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: PaymentProvider): Long =
            item.ID.toLong()

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<PaymentProvider> {

            override fun onBind(element: PaymentProvider) {
                binding.apply {
                    paymentProviderTextView.text = element.NAME
                }
                itemView.setOnClickListener { onClicked.invoke(element) }
            }
        }
    }
}