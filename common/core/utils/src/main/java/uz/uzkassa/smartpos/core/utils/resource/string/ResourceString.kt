package uz.uzkassa.smartpos.core.utils.resource.string

import uz.uzkassa.smartpos.core.utils.resource.ResourceType
import java.io.Serializable

sealed class ResourceString : Serializable, ResourceType

data class CompoundResourceString(val strings: List<ResourceString>) : ResourceString()

internal object EmptyString : ResourceString()

data class StringResource internal constructor(
    internal val resId: Int,
    internal val quantity: Int? = null,
    internal val anyArgs: List<Any>? = null,
    internal val resourceArgs: List<ResourceString>? = null
) : ResourceString() {
    internal var format: (CharSequence) -> CharSequence = { it }; private set

    constructor(
        resId: Int
    ) : this(resId = resId, anyArgs = emptyList(), resourceArgs = null)

    constructor(
        resId: Int,
        vararg args: Any
    ) : this(resId = resId, anyArgs = args.toList(), resourceArgs = null)

    constructor(
        resId: Int,
        quantity: Int
    ) : this(resId = resId, quantity = quantity, anyArgs = emptyList(), resourceArgs = null)

    constructor(
        resId: Int,
        quantity: Int,
        vararg args: Any
    ) : this(resId = resId, quantity = quantity, anyArgs = args.toList(), resourceArgs = null)

    constructor(
        resId: Int,
        vararg args: ResourceString
    ) : this(resId = resId, anyArgs = null, resourceArgs = args.toList())

    constructor(
        resId: Int,
        quantity: Int,
        vararg args: ResourceString
    ) : this(resId = resId, quantity = quantity, anyArgs = null, resourceArgs = args.toList())

    fun setFormat(charSequence: (CharSequence) -> CharSequence): ResourceString {
        format = charSequence
        return this
    }
}

internal data class SpecificString(val value: String) : ResourceString()