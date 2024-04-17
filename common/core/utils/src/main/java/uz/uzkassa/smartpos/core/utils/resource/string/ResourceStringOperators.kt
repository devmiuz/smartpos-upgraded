package uz.uzkassa.smartpos.core.utils.resource.string

operator fun CompoundResourceString.plus(another: CompoundResourceString) =
    CompoundResourceString(strings + another.strings)

operator fun CompoundResourceString.plus(another: ResourceString) =
    CompoundResourceString(strings + another)

operator fun CompoundResourceString.plus(another: String) =
    this + SpecificString(another)

operator fun ResourceString.plus(another: CompoundResourceString) =
    CompoundResourceString(listOf(this) + another.strings)

operator fun ResourceString.plus(another: ResourceString) =
    CompoundResourceString(listOf(this, another))

operator fun ResourceString.plus(another: String) =
    this + SpecificString(another)