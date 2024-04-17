package uz.uzkassa.smartpos.feature.branch.delete.data.exception

internal data class BranchDeleteException(
    val confirmationCodeNotDefined: Boolean,
    val reasonNotDefined: Boolean
) : Exception() {

    val isPassed: Boolean
        get() = confirmationCodeNotDefined || reasonNotDefined
}