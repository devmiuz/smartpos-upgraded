package uz.uzkassa.smartpos.feature.account.recovery.password.dependencies

interface AccountRecoveryPasswordFeatureCallback {

    fun onBackFromRecoveryPassword()

    fun onFinishRecoveryPassword()
}