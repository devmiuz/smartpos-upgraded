package uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEventDelegate

@Deprecated(message = "REMOVE", level = DeprecationLevel.ERROR)
abstract class RecyclerViewEndlessScrollDelegate<T : Any>(
    target: LifecycleOwner?,
    private val endlessListener: (page: Int, totalItemsCount: Int) -> Unit
) : RecyclerViewStateEventDelegate<T>(target) {
    override fun onSetupRecyclerView(manager: RecyclerView.LayoutManager?) {
        view?.addOnScrollListener(object :
            EndlessScrollListener(checkNotNull(manager)) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                endlessListener.invoke(page, totalItemsCount)
            }
        })
    }
}