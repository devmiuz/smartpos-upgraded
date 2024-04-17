package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card.CardTypeSelectionFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleCardTypeSelectionBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: CardTypeSelectionFragment,
    private inline val listener: (Type) -> Unit
) : RecyclerViewStateEventDelegate<Type>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(listener)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf()

    private class Adapter(
        private inline val listener: (Type) -> Unit
    ) : RecyclerViewAdapter<Type, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: Type): Long =
            item.ordinal.toLong()

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<Type> {

            override fun onBind(element: Type) {
                binding.apply {
                    itemView.setOnClickListener { listener.invoke(element) }

                    when (element) {
                        Type.CARD -> {
                            cardTypeImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_card_other)
                            cardTypeTextView.setText(R.string.core_presentation_common_payment_type_other)
                        }

                        Type.HUMO -> {
                            cardTypeImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_card_other)
                            cardTypeTextView.text = element.name
                        }

                        else -> {
                            cardTypeImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_card_other)
                            cardTypeTextView.text = element.name
                        }
                    }
                }
            }
        }

    }
}