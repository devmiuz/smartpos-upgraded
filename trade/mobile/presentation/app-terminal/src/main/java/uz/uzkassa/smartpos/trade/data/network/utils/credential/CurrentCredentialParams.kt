package uz.uzkassa.smartpos.trade.data.network.utils.credential

import android.util.Log
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import javax.inject.Inject

class CurrentCredentialParams @Inject constructor(
    accountAuthPreference: AccountAuthPreference,
    userAuthPreference: UserAuthPreference
) : CredentialParams {
    private val accountCredentialParams: AccountCredentialParams =
        AccountCredentialParams(accountAuthPreference)
    private val userCredentialParams: UserCredentialParams =
        UserCredentialParams(userAuthPreference)

    private val params: CredentialParams
        get() = if (userCredentialParams.isResumed) userCredentialParams else accountCredentialParams

    override var accessToken: String?
        get() = params.accessToken
        set(value) {
            params.accessToken = value
        }

    override val basicAuth: String
        get() = params.basicAuth

    override var refreshToken: String
        get() = params.refreshToken
        set(value) {
            params.refreshToken = value
        }

    override val isResumed: Boolean
        get() = params.isResumed


    private class AccountCredentialParams(
        private val accountAuthPreference: AccountAuthPreference
    ) : CredentialParams {

        override var accessToken: String?
            get() = accountAuthPreference.accessToken
            set(value) {
                accountAuthPreference.accessToken = value
            }

        override val basicAuth: String
            get() = accountAuthPreference.basicAuth

        override var refreshToken: String
            get() = accountAuthPreference.refreshToken
            set(value) {
                accountAuthPreference.refreshToken = value
            }

        override val isResumed: Boolean
            get() = accountAuthPreference.isResumed
    }

    private class UserCredentialParams(
        private val userAuthPreference: UserAuthPreference
    ) : CredentialParams {

        override var accessToken: String?
            get() = userAuthPreference.accessToken
            set(value) {
                userAuthPreference.accessToken = value
            }

        override val basicAuth: String
            get() = userAuthPreference.basicAuth

        override var refreshToken: String
            get() = userAuthPreference.refreshToken
            set(value) {
                userAuthPreference.refreshToken = value
            }

        override val isResumed: Boolean
            get() = userAuthPreference.isResumed
    }
}