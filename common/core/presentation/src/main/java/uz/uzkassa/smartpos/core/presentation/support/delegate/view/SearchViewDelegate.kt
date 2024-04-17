package uz.uzkassa.smartpos.core.presentation.support.delegate.view

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
open class SearchViewDelegate(
    target: LifecycleOwner,
    private val listener: SearchViewActionsListener
) : ViewDelegate<SearchView>(target), SearchView.OnCloseListener,
    SearchView.OnQueryTextListener {

    override fun onCreate(view: SearchView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.apply {
            setIconifiedByDefault(true)
            isIconified = false
            clearFocus()
            setOnCloseListener(this@SearchViewDelegate)
            setOnQueryTextListener(this@SearchViewDelegate)
        }
    }

    override fun onDestroy() {
        view?.clearFocus()
        super.onDestroy()
    }

    final override fun onClose(): Boolean =
        listener.onSearchViewClose().let { true }

    final override fun onQueryTextSubmit(query: String?): Boolean =
        listener.onSearchViewQueryTextSubmit(query ?: "").let { true }

    final override fun onQueryTextChange(newText: String?): Boolean =
        listener.onSearchViewQueryTextChange(newText ?: "").let { true }

    fun clearQuery() {
        view?.setQuery("", false)
    }

    fun clearFocus() {
        view?.clearFocus()
    }

    interface SearchViewActionsListener {

        fun onSearchViewQueryTextChange(query: String)

        fun onSearchViewQueryTextSubmit(query: String)

        fun onSearchViewClose()
    }
}