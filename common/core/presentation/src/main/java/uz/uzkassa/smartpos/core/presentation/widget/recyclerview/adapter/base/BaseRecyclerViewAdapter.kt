package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.base

import androidx.recyclerview.widget.RecyclerView

interface BaseRecyclerViewAdapter<T> {

    val isEmpty: Boolean

    val isNotEmpty: Boolean
        get() = !isEmpty

    fun getAdapter(): RecyclerView.Adapter<*>

    fun containsInAdapter(item: T): Boolean

    fun getItem(position: Int): T

    fun getItemId(item: T): Long

    fun getItemPosition(item: T): Int

    fun set(collection: Collection<T>)

    fun add(item: T)

    fun add(position: Int, item: T)

    fun addAll(collection: Collection<T>)

    fun remove(item: T)

    fun update(item: T)

    fun update(position: Int, item: T)

    fun upsertAll(collection: Collection<T>)

    fun clear()
}