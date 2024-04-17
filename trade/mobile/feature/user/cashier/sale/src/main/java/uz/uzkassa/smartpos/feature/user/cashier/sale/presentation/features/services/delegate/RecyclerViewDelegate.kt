package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.delegate

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.service.Service
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.ViewHolderFeatureUserCashierSaleMainServiceBinding
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.ServicesFragment

internal class RecyclerViewDelegate(
    target: ServicesFragment,
    private val onServiceClicked: (Service) -> Unit
) : RecyclerViewStateEventDelegate<Service>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(onServiceClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(context, 2)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(ItemDecoration())

    private class Adapter(
        private val onServiceClicked: (Service) -> Unit
    ) : RecyclerViewAdapter<Service, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                ViewHolderFeatureUserCashierSaleMainServiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun getItemId(item: Service): Long =
            item.ordinal.toLong()

        inner class ViewHolder(
            private val binding: ViewHolderFeatureUserCashierSaleMainServiceBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<Service> {

            override fun onBind(element: Service) {
                binding.apply {
                    linearLayout.setOnClickListener { onServiceClicked.invoke(element) }
                    when (element) {
                        Service.APPS -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_apps)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_apps)
                        }

                        Service.CALCULATOR -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_calculator)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_calculator)
                        }

                        Service.CASH_OPERATIONS -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_cash_operations)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_encashment)
                        }

                        Service.GTPOS -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_gtpos)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_terminal)
                        }

                        Service.EXPENSE -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_expense)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_expense)
                        }

                        Service.REFUND -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_refund)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_refund)
                        }

                        Service.PHONE -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_phone)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_phone)
                        }

                        Service.PRODUCT_SEARCH -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_product_search)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_product_search)
                        }

                        Service.RECEIPT_DRAFT -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_receipt_draft)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_delayed_check)
                        }

                        Service.PROFIT -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_profit)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_profit)
                        }

                        Service.STOCKS -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_stocks)
                            serviceImageView.setImageResource(R.drawable.drawable_feature_user_cashier_sale_vector_drawable_stocks)
                        }

                        Service.ADVANCE_CREDIT -> {
                            serviceTextView.setText(R.string.viewholder_feature_user_cashier_sale_main_services_credit_advance)
                            serviceImageView.setImageResource(R.drawable.ic_credit_advane)
                        }
                    }
                }
            }
        }
    }

    private class ItemDecoration : RecyclerView.ItemDecoration() {

        @Volatile
        private var spaces: Pair<Int, Int>? = null

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            with(outRect) {
                val position = parent.getChildAdapterPosition(view) + 1
                val isOddPosition: Boolean = position % 2 != 0
                val isFirstRow: Boolean = position < 3

                getSpaces(parent.context).let {
                    top = if (isFirstRow) it.second else it.first
                    left = if (isOddPosition) it.second else it.first
                    right = if (isOddPosition) it.first else it.second
                    bottom = it.first
                }
            }
        }

        private fun getSpaces(context: Context): Pair<Int, Int> {
            return if (spaces != null) checkNotNull(spaces)
            else synchronized(this) {
                return if (spaces != null) checkNotNull(spaces)
                else {
                    val spaceIn =
                        context.resources.getDimensionPixelSize(R.dimen.fragment_feature_user_cashier_sale_space_in)
                    val spaceOut =
                        context.resources.getDimensionPixelSize(R.dimen.fragment_feature_user_cashier_sale_space_out)
                    spaces = Pair(first = spaceIn, second = spaceOut)
                    checkNotNull(spaces)
                }
            }
        }
    }
}