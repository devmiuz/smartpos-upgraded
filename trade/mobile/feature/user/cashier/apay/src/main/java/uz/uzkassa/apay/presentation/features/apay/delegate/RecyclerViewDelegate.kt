package uz.uzkassa.apay.presentation.features.apay.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.uzkassa.apay.data.model.card_list.CardListResponse
import uz.uzkassa.apay.databinding.ItemCardBinding
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder

internal class RecyclerViewDelegate(
    target: Fragment,
    private val onCardClicked: (CardListResponse) -> Unit
) : RecyclerViewStateEventDelegate<CardListResponse>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(onCardClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    private class Adapter(
        private val cardClicked: (CardListResponse) -> Unit
    ) : RecyclerViewAdapter<CardListResponse, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: CardListResponse): Long = 0

        inner class ViewHolder(
            private val binding: ItemCardBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<CardListResponse> {

            override fun onBind(element: CardListResponse) {
                binding.apply {
                    itemView.setOnClickListener { cardClicked(element) }
                    Picasso.get().load(element.cardBackground).into(backgroundImg)
                    Picasso.get().load(element.bankLogo).into(bankLogoImg)
                    cardNumberTv.text = element.number
                    cardNameTv.text = "Название карты: ${element.name}"
                }
            }
        }

    }
}