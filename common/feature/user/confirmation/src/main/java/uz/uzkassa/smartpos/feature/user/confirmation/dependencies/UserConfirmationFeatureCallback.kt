package uz.uzkassa.smartpos.feature.user.confirmation.dependencies

interface UserConfirmationFeatureCallback {

    fun onFinishUserConfirmation(isConfirmed: Boolean)
}