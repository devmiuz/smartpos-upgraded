package uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

interface UserAuthPreference {

    var accessToken: String?

    var refreshToken: String

    val basicAuth: String

    val isResumed: Boolean
        get() = !accessToken.isNullOrEmpty() && basicAuth.isNotEmpty()

    fun setBasicAuth(phoneNumber: String, password: String)

    fun clearAll()

    companion object {

        fun instantiate(preferenceSource: PreferenceSource): UserAuthPreference =
            UserAuthPreferenceImpl(preferenceSource)
    }
}