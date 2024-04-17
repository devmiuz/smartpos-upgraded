package uz.uzkassa.smartpos.core.utils.resource.string

import android.content.Context

fun ResourceString.get(context: Context): CharSequence = when (this) {
    EmptyString -> ""
    is CompoundResourceString -> strings.joinToString(separator = "") { it.get(context) }
    is SpecificString -> value
    is StringResource -> format(
        getString(
            context = context,
            id = resId,
            quantity = quantity,
            args = anyArgs ?: resourceArgs?.map { it.get(context) } ?: listOf()
        )
    )
}

fun ResourceString.get(): CharSequence? = when (this) {
    EmptyString -> ""
    is CompoundResourceString -> strings.joinToString(separator = "") { it.get() ?: " " }
    is SpecificString -> value
    is StringResource -> null
}

fun String.asResource(): ResourceString =
    SpecificString(this)

private fun getString(context: Context, id: Int, quantity: Int?, args: List<Any>): CharSequence =
    if (quantity == null) context.getString(id, *args.toTypedArray())
    else context.resources.getQuantityString(id, quantity, *args.toTypedArray())