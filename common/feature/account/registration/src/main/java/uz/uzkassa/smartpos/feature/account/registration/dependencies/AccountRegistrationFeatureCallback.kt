package uz.uzkassa.smartpos.feature.account.registration.dependencies

interface AccountRegistrationFeatureCallback {
    fun onOpenTermsOfUsage(url: String)

    fun onBackFromRegistration()

    fun onFinishRegistration()
}