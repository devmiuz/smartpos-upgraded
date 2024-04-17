package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base

import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder

abstract class RecyclerViewAsyncAdapter<T : Any, VH : RecyclerView.ViewHolder> private constructor(
    config: AsyncDifferConfig<T>?, diffCallback: DiffUtil.ItemCallback<T>?
) : RecyclerView.Adapter<VH>(), BaseRecyclerViewAdapter<T> {

    constructor(config: AsyncDifferConfig<T>) : this(config, null)

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : this(null, diffCallback)

    constructor() : this(null, null)

    private val differ: AsyncListDiffer<T> by lazy {
        return@lazy when {
            config != null -> AsyncListDiffer(AdapterListUpdateCallback(this), config)
            else -> AsyncListDiffer(
                AdapterListUpdateCallback(this),
                AsyncDifferConfig.Builder(diffCallback ?: ItemCallback(this)).build()
            )
        }.also { it.addListListener(listener) }
    }

    private val listener: ListListener<T> = ListListener<T> { previousList, currentList ->
        onCurrentListChanged(previousList, currentList)
    }

    private val mutableList: MutableList<T>
        get() = ArrayList(differ.currentList)

    protected val list: List<T>
        get() = differ.currentList

    override val isEmpty: Boolean
        get() = list.isEmpty()

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: VH, position: Int) {
        (holder as? ViewHolderItemBinder<T>)?.onBind(getItem(position))
    }

    override fun findRelativeAdapterPositionIn(
        adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
        viewHolder: RecyclerView.ViewHolder,
        localPosition: Int
    ): Int = adapter.findRelativeAdapterPositionIn(adapter, viewHolder, localPosition)

    @Suppress("UNCHECKED_CAST")
    override fun onViewDetachedFromWindow(holder: VH) {
        (holder as? ViewHolderItemBinder<T>)?.onUnbind()
    }

    final override fun getItemId(position: Int): Long =
        getItemId(getItem(position))

    override fun getItemCount(): Int =
        list.size

    override fun getAdapter(): RecyclerView.Adapter<*> =
        this

    override fun containsInAdapter(item: T): Boolean =
        getItemPosition(item) != -1

    override fun getItem(position: Int): T =
        list[position]

    override fun getItemPosition(item: T): Int {
        val itemId: Long = getItemId(item)
        var position: Int = -1

        for (i: Int in mutableList.indices) {
            val currentItem: T = mutableList[i]
            if (getItemId(currentItem) == itemId) {
                position = i
                break
            }
        }

        return position
    }

    override fun set(collection: Collection<T>) {
        differ.apply { submitList(null); submitList(ArrayList(collection)) }
    }

    override fun add(item: T) =
        differ.submitList(mutableList.apply { add(item) })

    override fun add(position: Int, item: T) =
        differ.submitList(mutableList.apply { add(position, item) })

    override fun addAll(collection: Collection<T>) =
        differ.submitList(ArrayList(collection))

    override fun remove(item: T) =
        differ.submitList(mutableList.apply { remove(item) })

    override fun update(item: T) {
        val position: Int = getItemPosition(item)
        if (position >= 0)
            differ.submitList(mutableList.apply { removeAt(position); add(position, item) })
    }

    override fun update(position: Int, item: T) {
        val currentPosition: Int = getItemPosition(item)
        if (currentPosition >= 0)
            differ.submitList(mutableList.apply { removeAt(currentPosition); add(position, item) })
    }

    override fun upsertAll(collection: Collection<T>) {
        val adapterCollection: MutableList<T> = list.toMutableList()
        val upsertCollection: MutableList<T> = mutableListOf()
        val removeCollection: MutableList<T> = mutableListOf()

        collection.indices.forEach {
            val position: Int = it
            val element: T = collection.elementAt(it)
            var isContainsInAdapter = false

            for (adapterElement: T in list) {
                if (getItemId(element) == getItemId(adapterElement)) {
                    isContainsInAdapter = true
                    break
                }
            }

            if (!isContainsInAdapter) {
                upsertCollection.add(element)
                add(position, element)
            } else {
                upsertCollection.add(element)
                update(position, element)
            }
        }

        adapterCollection.forEach { adapterElement ->
            upsertCollection.forEach { item ->
                if (getItemId(adapterElement) != getItemId(item))
                    removeCollection.add(adapterElement)
            }
        }

        adapterCollection.let { it ->
            it.removeAll(removeCollection)
            it.forEach { remove(it) }
        }
    }

    override fun clear() =
        differ.submitList(null)

    protected open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {
    }

    private class ItemCallback<T : Any>(
        private val adapter: BaseRecyclerViewAdapter<T>
    ) : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            adapter.getItemId(oldItem) == adapter.getItemId(newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem
    }
}