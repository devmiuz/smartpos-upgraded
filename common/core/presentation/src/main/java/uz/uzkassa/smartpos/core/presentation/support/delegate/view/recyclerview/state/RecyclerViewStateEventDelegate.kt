package uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.RecyclerViewDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state.RecyclerViewStateEvent.*
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base.BaseRecyclerViewAdapter
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.state.RecyclerViewStateAdapter
import kotlin.properties.Delegates
import kotlin.reflect.KClass

@Suppress("MemberVisibilityCanBePrivate")
abstract class RecyclerViewStateEventDelegate<T : Any>(
    target: Any?,
    lifecycleOwner: LifecycleOwner?
) : RecyclerViewDelegate<T>(target, lifecycleOwner) {

    constructor(activity: ComponentActivity) : this(activity, activity)

    constructor(fragment: Fragment) : this(fragment, fragment)

    constructor(lifecycleOwner: LifecycleOwner?) : this(null, lifecycleOwner)

    private val contentStateAdapter: RecyclerViewStateAdapter?
        get() = (view?.adapter as? ConcatAdapter)?.adapters?.get(0) as? RecyclerViewStateAdapter

    private val footerStateAdapter: RecyclerViewStateAdapter?
        get() = (view?.adapter as? ConcatAdapter)?.adapters?.get(2) as? RecyclerViewStateAdapter

    private var loadingStateKClass: KClass<out RecyclerViewStateEvent> by Delegates.notNull()
    private var emptyStateKClass: KClass<out RecyclerViewStateEvent> by Delegates.notNull()
    private var errorStateKClass: KClass<out RecyclerViewStateEvent> by Delegates.notNull()

    @Suppress("UNCHECKED_CAST")
    private val baseRecyclerViewAdapter: BaseRecyclerViewAdapter<T>?
        get() = recyclerViewAdapter as? BaseRecyclerViewAdapter<T>

    override val recyclerViewAdapter: RecyclerView.Adapter<*>
        get() = (view?.adapter as? ConcatAdapter)?.adapters?.find { it is BaseRecyclerViewAdapter<*> }
            ?: throw IllegalStateException("Adapter is not defined in delegate")

    private val currentStateAdapter: RecyclerViewStateAdapter?
        get() = if (isEmpty) contentStateAdapter else footerStateAdapter

    override val isEmpty: Boolean
        get() = baseRecyclerViewAdapter?.isEmpty ?: false

    override val isNotEmpty: Boolean
        get() = baseRecyclerViewAdapter?.isNotEmpty ?: false

    final override fun getAdapter(): RecyclerView.Adapter<*> =
        ConcatAdapter(RecyclerViewStateAdapter(), getItemsAdapter(), RecyclerViewStateAdapter())

    override fun onCreate(view: RecyclerView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        val loadingStateEvent: RecyclerViewStateEvent? =
            getLoadingState()?.also { loadingStateKClass = it::class }
        val emptyStateEvent: RecyclerViewStateEvent? =
            getEmptyState()?.also { emptyStateKClass = it::class }
        val errorStateEvent: RecyclerViewStateEvent? =
            getErrorState()?.also { errorStateKClass = it::class }

        loadingStateEvent?.let { registerContentState(it); registerFooterState(it) }
        emptyStateEvent?.let { registerContentState(it); registerFooterState(it) }
        errorStateEvent?.let { registerContentState(it); registerFooterState(it) }
        onSetupRecyclerView(getLayoutManager().also { view.layoutManager = it })
    }

    override fun containsInAdapter(item: T): Boolean =
        baseRecyclerViewAdapter?.containsInAdapter(item) ?: false

    override fun getItem(position: Int): T? =
        baseRecyclerViewAdapter?.getItem(position)

    override fun getPosition(item: T): Int =
        baseRecyclerViewAdapter?.getItemPosition(item) ?: 0

    override fun set(collection: Collection<T>) {
        baseRecyclerViewAdapter?.set(collection)
    }

    override fun add(item: T) {
        clearEmptyState()
        baseRecyclerViewAdapter?.add(item)
    }

    override fun add(position: Int, item: T) {
        clearEmptyState()
        baseRecyclerViewAdapter?.add(position, item)
    }

    override fun addAll(collection: Collection<T>) {
        clearEmptyState(collection.isNotEmpty())
        baseRecyclerViewAdapter?.addAll(collection)
    }

    override fun upsertAll(collection: Collection<T>) {
        clearEmptyState(collection.isNotEmpty())
        baseRecyclerViewAdapter?.upsertAll(collection)
    }

    override fun clear() {
        clearState()
        baseRecyclerViewAdapter?.clear()
    }

    override fun remove(item: T) {
        baseRecyclerViewAdapter?.remove(item)
    }

    override fun update(item: T) {
        baseRecyclerViewAdapter?.update(item)
    }

    override fun update(position: Int, item: T) {
        baseRecyclerViewAdapter?.update(position, item)
    }

    open fun onLoading() =
        setState(loadingStateKClass)

    open fun onSuccess(collection: Collection<T>) =
        onSuccess(collection, ItemChangesBehavior.ADD)

    protected open fun onSuccess(collection: Collection<T>, behavior: ItemChangesBehavior) {
        if (isEmpty && collection.isEmpty()) setState(emptyStateKClass)
        else {
            clearState()
            when (behavior) {
                ItemChangesBehavior.ADD -> addAll(collection)
                ItemChangesBehavior.SET -> set(collection)
                ItemChangesBehavior.UPSERT -> upsertAll(collection)
            }
        }
    }

    open fun onFailure(throwable: Throwable, onRetryClickListener: (() -> Unit)? = null) {
        val errorStateEvent: ErrorRecyclerViewStateEvent? =
            findState(errorStateKClass) as? ErrorRecyclerViewStateEvent

        errorStateEvent?.setError(throwable, onRetryClickListener)
        setState(errorStateKClass)
    }

    private fun clearEmptyState(precision: Boolean = true) {
        if (precision && currentStateAdapter?.getCurrentState() is EmptyRecyclerViewStateEvent)
            currentStateAdapter?.clearState()
    }

    protected fun registerContentState(event: RecyclerViewStateEvent) {
        contentStateAdapter?.registerState(event)
    }

    protected fun registerFooterState(event: RecyclerViewStateEvent) {
        footerStateAdapter?.registerState(event)
    }

    protected open fun onSetupRecyclerView(manager: RecyclerView.LayoutManager?) {
    }

    protected open fun getLoadingState(): RecyclerViewStateEvent? =
        LoadingRecyclerViewStateEvent()

    protected open fun getEmptyState(): RecyclerViewStateEvent? =
        EmptyRecyclerViewStateEvent()

    protected open fun getErrorState(): RecyclerViewStateEvent? =
        ErrorRecyclerViewStateEvent()

    protected fun <S : RecyclerViewStateEvent> findState(kClass: KClass<out S>): S? {
        return currentStateAdapter?.findState(kClass)
    }

    protected fun setState(kClass: KClass<out RecyclerViewStateEvent>) {
        findState(kClass)?.isContent = isEmpty
        currentStateAdapter?.setState(kClass)
    }

    protected fun clearState() {
        currentStateAdapter?.clearState()
    }

    protected abstract fun getItemsAdapter(): RecyclerView.Adapter<*>

    protected enum class ItemChangesBehavior {
        ADD, SET, UPSERT
    }
}