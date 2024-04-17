package uz.uzkassa.smartpos.feature.product.saving.data.exception

internal data class ProductSavingException(
    val isBarcodeNotDefined: Boolean,
    val isCategoryNotDefined: Boolean,
    val isNameNotDefined: Boolean,
    val isPriceNotDefined: Boolean,
    val isUnitNotDefined: Boolean
) : Throwable() {

    val isPassed: Boolean
        get() = isBarcodeNotDefined || isCategoryNotDefined || isNameNotDefined || isPriceNotDefined || isUnitNotDefined
}