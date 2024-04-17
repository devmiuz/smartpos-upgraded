package uz.uzkassa.smartpos.core.utils.collections

fun <T> MutableCollection<T>.replaceTo(element: T, predicate: (T) -> Boolean): Boolean {
    removeBy(predicate)
    return add(element)
}

fun <T> MutableCollection<T>.removeBy(predicate: (T) -> Boolean): Boolean {
    val element: T? = find(predicate)
    return remove(element)
}

fun <T> MutableList<T>.replace(oldElement: T, newElement: T): Boolean {
    val index: Int = indexOf(oldElement)
    val removed: Boolean = remove(oldElement)
    add(index, newElement)
    return removed
}

fun <T> MutableList<T>.replaceTo(element: T, predicate: (T) -> Boolean): Boolean {
    val index: Int = indexOf(find(predicate))
    val removed: Boolean = removeBy(predicate)
    add(index, element)
    return removed
}