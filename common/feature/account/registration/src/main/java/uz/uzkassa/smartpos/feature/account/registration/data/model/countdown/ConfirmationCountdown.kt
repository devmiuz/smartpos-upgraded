package uz.uzkassa.smartpos.feature.account.registration.data.model.countdown

internal data class ConfirmationCountdown(
    val period: Long,
    val countdown: Long,
    val isCompleted: Boolean
)