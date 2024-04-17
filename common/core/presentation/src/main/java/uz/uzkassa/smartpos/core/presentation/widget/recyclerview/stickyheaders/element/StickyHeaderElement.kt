package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.stickyheaders.element

import com.brandongogetap.stickyheaders.exposed.StickyHeader

open class StickyHeaderElement<T>(
    private val elementId: Long,
    override val value: T
) : Element<T>, StickyHeader {

    override val id: Long
        get() = elementId + value.hashCode().toLong()
}