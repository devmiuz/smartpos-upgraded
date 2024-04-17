package uz.uzkassa.smartpos.feature.launcher.presentation.features.language.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.decoration.space.SpacesItemDecoration
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.databinding.ViewHolderFeatureLanguageSelectionLanguageBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: Fragment,
    private val languageClickListener: (Language) -> Unit
) : RecyclerViewDelegate<Language>(target) {

    override fun getAdapter(): RecyclerView.Adapter<*> =
        Adapter { languageClickListener.invoke(it) }

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(SpacesItemDecoration(R.dimen._1sdp, R.dimen._14sdp))

    private class Adapter(
        private val languageClickListener: (Language) -> Unit
    ) : RecyclerViewAdapter<Language, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: Language): Long =
            item.ordinal.toLong()

        private inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<Language> {

            override fun onBind(element: Language) {
                binding.apply {
                    languageButton.apply {
                        text = element.languageName
                        setOnClickListener { languageClickListener.invoke(element) }
                    }
                }
            }

        }
    }
}