package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.city.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.regioncity.selection.databinding.ViewHolderFeatureRegionCityCitySelectionBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    private val cityClicked: (City) -> Unit
) : RecyclerViewStateEventDelegate<City>(target) {

    override fun getItemsAdapter(): RecyclerViewAdapter<City, *> =
        Adapter(cityClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onLoading() {
        clear()
        super.onLoading()
    }

    private class Adapter(
        private val cityClicked: (City) -> Unit
    ) : RecyclerViewAdapter<City, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: City): Long =
            item.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<City> {

            override fun onBind(element: City) {
                binding.apply {
                    constraintLayout.setOnClickListener { cityClicked.invoke(element) }
                    cityNameTextView.text = element.nameRu
                }
            }
        }
    }
}