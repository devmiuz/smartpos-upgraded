package uz.uzkassa.smartpos.feature.account.registration.data.model.code

internal data class ConfirmationCode(
    val phoneNumber: String,
    val availableResendCount: Int,
    val isResendAvailable: Boolean
)