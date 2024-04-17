package uz.uzkassa.smartpos.feature.user.settings.password.data.exception

internal data class PasswordChangeException(
    val isCurrentPasswordNotDefined: Boolean,
    val isCurrentPasswordNotValid: Boolean,
    val isNewPasswordNotDefined: Boolean,
    val isNewPasswordNotValid: Boolean
) : Exception() {

    val isPassed: Boolean
        get() = isCurrentPasswordNotDefined || isCurrentPasswordNotValid ||
                isNewPasswordNotDefined || isNewPasswordNotValid
}