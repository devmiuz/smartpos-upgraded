package uz.uzkassa.smartpos.feature.user.settings.language.presentation.delegate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.RecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder
import uz.uzkassa.smartpos.feature.user.settings.language.data.model.LanguageSelection
import uz.uzkassa.smartpos.feature.user.settings.language.presentation.UserLanguageChangeFragment
import uz.uzkassa.smartpos.feature.user.settings.language.databinding.ViewHolderFeatureUserLanguageChangeBinding as ViewBinding

internal class RecyclerViewDelegate(
    target: UserLanguageChangeFragment,
    private val languageClicked: (Language, Boolean) -> Unit
) : RecyclerViewStateEventDelegate<LanguageSelection>(target) {

    override fun getItemsAdapter(): RecyclerView.Adapter<*> =
        Adapter(languageClicked)

    override fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    override fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? =
        arrayOf(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.itemAnimator = null
    }

    private class Adapter(
        private val languageClicked: (Language, Boolean) -> Unit
    ) : RecyclerViewAdapter<LanguageSelection, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemId(item: LanguageSelection): Long = when (item.language) {
            Language.RUSSIAN -> 1L
            Language.UZBEK -> 2L
            else -> 0L
        }

        inner class ViewHolder(
            private val binding: ViewBinding
        ) : RecyclerView.ViewHolder(binding.root),
            ViewHolderItemBinder<LanguageSelection> {

            override fun onBind(element: LanguageSelection) {
                binding.apply {
                    languageLinearLayout.setOnClickListener {
                        languageClicked.invoke(element.language, !element.isSelected)
                    }
                    languageImageView.visibility = if (element.isSelected) VISIBLE else INVISIBLE
                    languageTextView.text = element.language.languageName
                }
            }
        }

    }
}