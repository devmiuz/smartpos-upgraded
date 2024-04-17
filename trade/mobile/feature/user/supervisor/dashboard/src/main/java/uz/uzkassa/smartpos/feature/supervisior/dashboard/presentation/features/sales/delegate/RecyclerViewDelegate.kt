package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.delegate

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.supervisior.dashboard.R
import uz.uzkassa.smartpos.feature.supervisior.dashboard.databinding.ViewHolderSalesDynamicsBinding
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales.wrapper.SalesDynamicsWrapper

internal class RecyclerViewDelegate(
    target: Fragment,
    private val listener: (SalesDynamicsWrapper) -> Unit
) : RecyclerViewStateEventDelegate<SalesDynamicsWrapper>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter { listener.invoke(it) }

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(ItemDecoration())

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(view)
        view.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (isEmpty) return
                    val layoutManager: LinearLayoutManager =
                        recyclerView.layoutManager as? LinearLayoutManager ?: return
                    val snapView: View =
                        pagerSnapHelper.findSnapView(recyclerView.layoutManager) ?: return
                    val element: SalesDynamicsWrapper =
                        getItem(layoutManager.getPosition(snapView)) ?: return
                    listener.invoke(element)
                }
            }
        )
    }

    override fun onLoading() {
        clear()
        super.onLoading()
    }

    override fun onSuccess(collection: Collection<SalesDynamicsWrapper>) {
        onSuccess(collection, ItemChangesBehavior.SET)
        if (collection.isNotEmpty()) view?.scrollToPosition(collection.size - 1)
    }

    private class Adapter(
        private val listener: (SalesDynamicsWrapper) -> Unit
    ) : RecyclerViewAdapter<SalesDynamicsWrapper, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                ViewHolderSalesDynamicsBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

        override fun getItemId(item: SalesDynamicsWrapper): Long =
            item.salesDynamics.salesDate.time

        inner class ViewHolder(
            private val binding: ViewHolderSalesDynamicsBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<SalesDynamicsWrapper> {

            override fun onBind(element: SalesDynamicsWrapper) {
                itemView.setOnClickListener { listener.invoke(element) }

                binding.apply {
                    salesDynamicsOddmentTextView.text =
                        element.salesDynamics.salesDate.toString("dd.MM")
                    ConstraintSet()
                        .apply {
                            clone(constraintLayout)
                            setVerticalWeight(
                                R.id.sales_dynamics_current_text_view,
                                element.percentOfMaxSalesTotal
                            )
                            setVerticalWeight(
                                R.id.sales_dynamics_oddment_text_view,
                                1 - element.percentOfMaxSalesTotal
                            )
                        }
                        .applyTo(constraintLayout)
                }
            }
        }
    }

    private class ItemDecoration : RecyclerView.ItemDecoration() {

        @Volatile
        private var spaces: Pair<Int, Int>? = null

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                val position = parent.getChildAdapterPosition(view) + 1
                val itemCount = state.itemCount
                val isLastItem: Boolean = position == itemCount
                val isFirstItem: Boolean = position == 1

                getSpaces(parent.context).let {
                    top = 0
                    left = if (isFirstItem) it.second else it.first
                    right = if (isLastItem) it.second else it.first
                    bottom = 0
                }
            }
        }

        private fun getSpaces(context: Context): Pair<Int, Int> {
            return if (spaces != null) checkNotNull(spaces)
            else synchronized(this) {
                return if (spaces != null) checkNotNull(spaces)
                else {
                    val spaceIn: Int = context.resources.getDimensionPixelSize(R.dimen._8sdp)
                    val spaceOut: Int = context.resources.getDimensionPixelSize(R.dimen._112sdp)
                    spaces = Pair(first = spaceIn, second = spaceOut)
                    checkNotNull(spaces)
                }
            }
        }
    }
}