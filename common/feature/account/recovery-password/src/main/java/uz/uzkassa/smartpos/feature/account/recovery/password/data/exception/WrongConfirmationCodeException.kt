package uz.uzkassa.smartpos.feature.account.recovery.password.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.account.recovery.password.R

internal class WrongConfirmationCodeException : Exception(), LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_account_recovery_password_new_password_error_wrong_activation_code_title)
}