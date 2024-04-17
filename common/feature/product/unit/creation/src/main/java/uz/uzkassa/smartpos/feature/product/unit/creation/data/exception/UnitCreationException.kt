package uz.uzkassa.smartpos.feature.product.unit.creation.data.exception

internal data class UnitCreationException(
    val isUnitCountNotDefined: Boolean,
    val isUnitNotDefined: Boolean
) : Throwable() {

    val isPassed: Boolean
        get() = isUnitCountNotDefined || isUnitNotDefined
}