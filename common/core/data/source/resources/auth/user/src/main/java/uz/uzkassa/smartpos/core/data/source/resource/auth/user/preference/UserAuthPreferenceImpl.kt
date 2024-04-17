package uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference

import android.util.Base64
import android.util.Base64.encodeToString
import uz.uzkassa.smartpos.core.data.source.preference.PreferenceSource

internal class UserAuthPreferenceImpl(
    private val preferenceSource: PreferenceSource
) : UserAuthPreference {

    override var accessToken: String?
        get() = preferenceSource.getString(PREFERENCE_STRING_ACCESS_TOKEN, "")
            .let { return@let if (it.isEmpty()) null else "Bearer $it" }
        set(value) {
            if (value != null) preferenceSource.setString(PREFERENCE_STRING_ACCESS_TOKEN, value)
            else preferenceSource.clear(PREFERENCE_STRING_ACCESS_TOKEN)
        }

    override var refreshToken: String
        get() = preferenceSource.getString(PREFERENCE_STRING_REFRESH_TOKEN, "")
        set(value) {
            preferenceSource.setString(PREFERENCE_STRING_REFRESH_TOKEN, value)
        }

    @Suppress("SpellCheckingInspection")
    override val basicAuth: String
        get() {
            val base64: String = encodeToString("web_app:changeit".toByteArray(), Base64.DEFAULT)
            return "Basic " + base64.replace("[\\n\\r]".toRegex(), "")
        }

    @Suppress("SpellCheckingInspection")
    override fun setBasicAuth(phoneNumber: String, password: String) {
        val base64: String = encodeToString("web_app:changeit".toByteArray(), Base64.DEFAULT)
        val basicAuth: String = "Basic " + base64.replace("[\\n\\r]".toRegex(), "")
        preferenceSource.setString(PREFERENCE_STRING_BASIC_AUTH, basicAuth)
    }

    override fun clearAll() =
        preferenceSource.clearAll()

    private companion object {
        const val PREFERENCE_STRING_ACCESS_TOKEN: String = "preference_string_access_token"
        const val PREFERENCE_STRING_BASIC_AUTH: String = "preference_string_basic_auth"
        const val PREFERENCE_STRING_REFRESH_TOKEN: String = "preference_string_refresh_token"
    }
}