package uz.uzkassa.smartpos.feature.account.auth.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.account.auth.R

internal class AccountNotFoundException : Exception(), LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_account_auth_login_error_unauthorized_message)
}