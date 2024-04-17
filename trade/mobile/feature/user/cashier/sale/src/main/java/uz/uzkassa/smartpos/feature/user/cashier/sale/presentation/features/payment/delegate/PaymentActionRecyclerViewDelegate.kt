package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.SpacesItemDecoration
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.payment.PaymentAction
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.SalePaymentFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSalePaymentDetailsActionsBinding as ViewBinding

internal class PaymentActionRecyclerViewDelegate(
    target: SalePaymentFragment,
    private inline val onServiceClicked: (PaymentAction) -> Unit
) : RecyclerViewDelegate<PaymentAction>(target) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter(onServiceClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(SpacesItemDecoration(R.dimen._2sdp, R.dimen._2sdp))

    private class Adapter(
        private inline val onServiceClicked: (PaymentAction) -> Unit
    ) : RecyclerViewAdapter<PaymentAction, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: PaymentAction): Long = item.ordinal.toLong()

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root), ViewHolderItemBinder<PaymentAction> {

            override fun onBind(element: PaymentAction) {
                binding.apply {
                    linearLayout.setOnClickListener { onServiceClicked.invoke(element) }

                    when (element) {
                        PaymentAction.APP -> {
                            imageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_app)
                            textView.setText(R.string.viewholder_feature_user_cashier_sale_payment_details_action_app)
                        }
                        PaymentAction.FISCAL_RECEIPT -> {
                            imageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_qr_receipt)
                            textView.setText(R.string.core_presentation_common_payment_type_card)
                        }
                        PaymentAction.DISCOUNT -> {
                            imageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_discount)
                            textView.setText(R.string.viewholder_feature_user_cashier_sale_payment_details_action_discount)
                        }
                        PaymentAction.RECEIPT_DRAFT -> {
                            imageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_receipt_draft)
                            textView.setText(R.string.viewholder_feature_user_cashier_sale_payment_details_action_receipt_check)
                        }
                        PaymentAction.SEND -> {
                            imageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_send_check)
                            textView.setText(R.string.viewholder_feature_user_cashier_sale_payment_details_action_send_check)
                        }
                        PaymentAction.ADVANCE -> {
                            imageView.setImageResource(R.drawable.ic_baseline_emoji_events_24)
                            textView.setText(R.string.viewholder_feature_user_cashier_sale_payment_details_action_advance)
                        }
                        PaymentAction.CREDIT -> {
                            imageView.setImageResource(R.drawable.ic_baseline_credit_card_24)
                            textView.setText(R.string.viewholder_feature_user_cashier_sale_payment_details_action_credit)
                        }
                    }
                }
            }
        }
    }
}