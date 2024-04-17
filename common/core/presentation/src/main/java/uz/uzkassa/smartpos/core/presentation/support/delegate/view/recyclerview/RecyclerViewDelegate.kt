package uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewDelegate
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.BaseRecyclerViewAdapter

@Suppress("unused", "UNCHECKED_CAST")
abstract class RecyclerViewDelegate<T : Any>(
    target: Any?,
    lifecycleOwner: LifecycleOwner?
) : ViewDelegate<RecyclerView>(target, lifecycleOwner) {

    constructor(activity: ComponentActivity) : this(activity, activity)

    constructor(fragment: Fragment) : this(fragment, fragment)

    constructor(lifecycleOwner: LifecycleOwner?) : this(null, lifecycleOwner)

    private val baseRecyclerViewAdapter: BaseRecyclerViewAdapter<T>?
        get() = view?.adapter as? BaseRecyclerViewAdapter<T>?

    protected open val recyclerViewAdapter: RecyclerView.Adapter<*>
        get() = baseRecyclerViewAdapter?.getAdapter() ?: view?.adapter
        ?: throw IllegalStateException("Adapter is not defined in delegate")

    protected open val isEmpty: Boolean
        get() = baseRecyclerViewAdapter?.isEmpty ?: false

    protected open val isNotEmpty: Boolean
        get() = baseRecyclerViewAdapter?.isNotEmpty ?: false

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.setHasFixedSize(true)

        view.also {
            if (it.adapter == null)
                it.adapter = getAdapter()

            if (it.layoutManager == null)
                it.layoutManager = getLayoutManager()
        }

        getItemDecoration()?.forEach { view.addItemDecoration(it) }
    }

    @CallSuper
    override fun onDestroy() {
        if (view?.adapter != null) view?.adapter = null
        super.onDestroy()
    }

    open fun containsInAdapter(item: T): Boolean =
        baseRecyclerViewAdapter?.containsInAdapter(item) ?: false

    open fun getItem(position: Int): T? =
        baseRecyclerViewAdapter?.getItem(position)

    open fun getPosition(item: T): Int =
        baseRecyclerViewAdapter?.getItemPosition(item) ?: 0

    open fun set(collection: Collection<T>) {
        baseRecyclerViewAdapter?.set(collection)
    }

    open fun add(item: T) {
        baseRecyclerViewAdapter?.add(item)
    }

    open fun add(position: Int, item: T) {
        baseRecyclerViewAdapter?.add(position, item)
    }

    open fun addAll(collection: Collection<T>) {
        baseRecyclerViewAdapter?.addAll(collection)
    }

    open fun upsertAll(collection: Collection<T>) {
        baseRecyclerViewAdapter?.upsertAll(collection)
    }

    open fun clear() {
        baseRecyclerViewAdapter?.clear()
    }

    open fun remove(item: T) {
        baseRecyclerViewAdapter?.remove(item)
    }

    open fun update(item: T) {
        baseRecyclerViewAdapter?.update(item)
    }

    open fun update(position: Int, item: T) {
        baseRecyclerViewAdapter?.update(position, item)
    }

    protected open fun getItemDecoration(): Array<RecyclerView.ItemDecoration>? = null

    protected abstract fun getAdapter(): RecyclerView.Adapter<*>

    protected abstract fun getLayoutManager(): RecyclerView.LayoutManager
}