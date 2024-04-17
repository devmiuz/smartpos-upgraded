package uz.uzkassa.smartpos.core.presentation.widget.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class DefaultBaseAdapter<T : Any>(private val context: Context) : BaseAdapter() {
    private val elements: MutableList<T> = mutableListOf()

    protected abstract val viewLayoutId: Int
    protected abstract val dropDownLayoutId: Int

    final override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View =
            if (convertView == null)
                LayoutInflater.from(context).inflate(viewLayoutId, parent, false)
                    .also { it.tag = ViewHolder(it) }
            else (convertView.tag as ViewHolder).view
        getView(position, view, getItem(position))
        return view
    }

    final override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View =
            if (convertView == null)
                LayoutInflater.from(context).inflate(dropDownLayoutId, parent, false)
                    .also { it.tag = ViewHolder(it) }
            else (convertView.tag as ViewHolder).view
        getDropDownView(position, view, getItem(position))
        return view
    }

    final override fun getItem(position: Int): T =
        elements[position]

    final override fun getItemId(position: Int): Long =
        getItemId(elements[position])

    final override fun getCount(): Int =
        elements.size

    fun getItemPosition(element: T): Int =
        elements.indexOf(element)

    fun add(element: T) {
        elements.add(element)
        notifyDataSetChanged()
    }

    fun addAll(collection: Collection<T>) {
        elements.addAll(collection)
        notifyDataSetChanged()
    }

    fun clear() {
        elements.clear()
        notifyDataSetChanged()
    }

    fun remove(element: T) {
        elements.remove(element)
        notifyDataSetChanged()
    }

    protected open fun getView(position: Int, view: View, element: T) {
    }

    protected open fun getDropDownView(position: Int, view: View, element: T) {
    }

    abstract fun getItemId(element: T): Long

    private class ViewHolder(val view: View)
}