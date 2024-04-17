package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.stickyheaders

import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.stickyheaders.element.Element
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.stickyheaders.element.StickyHeaderElement

open class StickyHeaderElementMapper<T : Any>(
    private val separateBy: (T) -> Any,
    private val headerElement: (T) -> StickyHeaderElement<*>,
    private val elementId: (T) -> Long
) {
    private var previousData: Any? = null

    fun map(collection: Collection<T>): Collection<Element<*>> {
        val list: MutableList<Element<*>> = ArrayList()

        collection.forEach {
            val any: Any = separateBy(it)
            if (previousData == null || previousData != any) {
                previousData = any
                list.add(headerElement(it))
            }
            list.add(getElement(elementId.invoke(it), it))
        }

        return list
    }

    fun reset() {
        previousData = null
    }

    private fun getElement(id: Long, element: T): Element<T> {
        return object : Element<T> {
            override val id: Long = id
            override val value: T = element
        }
    }
}