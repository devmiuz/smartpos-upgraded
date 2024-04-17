package uz.uzkassa.smartpos.feature.regioncity.selection.presentation.child.region.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.regioncity.selection.databinding.ViewHolderFeatureRegionCityRegionSelectionBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    private val onRegionClicked: (Region) -> Unit
) : RecyclerViewStateEventDelegate<Region>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(onRegionClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    
    private class Adapter(
        private val regionClicked: (Region) -> Unit
    ) : RecyclerViewAdapter<Region, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: Region): Long =
            item.id

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<Region> {

            override fun onBind(element: Region) {
                binding.apply {
                    constraintLayout.setOnClickListener { regionClicked(element) }
                    regionNameTextView.text = element.nameRu
                }
            }
        }

    }
}