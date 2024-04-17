package uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference

import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

interface AccountAuthPreference {

    var accessToken: String?

    var refreshToken: String

    val basicAuth: String

    val isResumed: Boolean
        get() = !accessToken.isNullOrEmpty() && basicAuth.isNotEmpty()

    fun setBasicAuth(phoneNumber: String, password: String)

    fun clearAll()

    companion object {

        fun instantiate(preferenceSource: PreferenceSource): AccountAuthPreference =
            AccountAuthPreferenceImpl(preferenceSource)
    }
}