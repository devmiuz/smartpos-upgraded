package uz.uzkassa.smartpos.feature.user.settings.data.data.exception

internal data class UserDataChangeException(
    val isLastNameNotDefined: Boolean,
    val isFirstNameNotDefined: Boolean
) : Exception() {

    val isPassed: Boolean
        get() = isLastNameNotDefined || isFirstNameNotDefined
}