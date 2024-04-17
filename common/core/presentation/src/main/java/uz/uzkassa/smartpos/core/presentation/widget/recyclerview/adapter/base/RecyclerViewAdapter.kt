package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base

import androidx.recyclerview.widget.RecyclerView
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder.ViewHolderItemBinder

abstract class RecyclerViewAdapter<T : Any, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<VH>(), BaseRecyclerViewAdapter<T> {
    private val mutableList: MutableList<T> = arrayListOf()

    val list: List<T>
        get() = mutableList

    override val isEmpty: Boolean
        get() = mutableList.isEmpty()

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: VH, position: Int) {
        (holder as? ViewHolderItemBinder<T>)?.onBind(mutableList[position])
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewDetachedFromWindow(holder: VH) {
        (holder as? ViewHolderItemBinder<T>)?.onUnbind()
    }

    final override fun getItemId(position: Int): Long =
        getItemId(getItem(position))

    override fun getItemCount(): Int =
        mutableList.size

    override fun getAdapter(): RecyclerView.Adapter<*> =
        this

    override fun containsInAdapter(item: T): Boolean =
        getItemPosition(item) != -1

    override fun getItem(position: Int): T =
        mutableList[position]

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

    override fun set(collection: Collection<T>) =
        mutableList.let { clear(); addAll(collection) }

    override fun add(item: T) {
        mutableList.add(item)
        notifyItemInserted(mutableList.size)
    }

    override fun add(position: Int, item: T) {
        mutableList.add(position, item)
        notifyItemInserted(position)
    }

    override fun addAll(collection: Collection<T>) {
        collection.forEach { element -> this.mutableList.add(element) }
        val newSize: Int = collection.size
        val oldSize: Int = this.mutableList.size - newSize
        notifyItemRangeInserted(oldSize, newSize)
    }

    override fun remove(item: T) {
        val position: Int = getItemPosition(item)
        if (position >= 0) {
            mutableList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun update(item: T) {
        val position: Int = getItemPosition(item)
        if (position >= 0) {
            mutableList.apply { removeAt(position); add(position, item) }
            notifyItemChanged(position)
        } else add(item)
    }

    override fun update(position: Int, item: T) {
        val currentPosition: Int = getItemPosition(item)
        if (currentPosition >= 0) {
            mutableList.apply { removeAt(currentPosition); add(position, item) }
            notifyItemChanged(position)
        }
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

    override fun clear() {
        mutableList.clear()
        notifyDataSetChanged()
    }
}