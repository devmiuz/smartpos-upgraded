package uz.uzkassa.smartpos.feature.user.settings.dependencies

interface UserSettingsFeatureCallback {

    fun onBackFromUserSettings()

    fun onOpenLanguageChangeScreen()

    fun onOpenPasswordChangingScreen()

    fun onOpenPersonalDataChangeScreen()

    fun onOpenPhoneNumberChangingScreen()
}