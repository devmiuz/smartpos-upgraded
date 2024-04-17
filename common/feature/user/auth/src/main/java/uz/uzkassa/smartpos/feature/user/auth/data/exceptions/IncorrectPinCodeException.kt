package uz.uzkassa.smartpos.feature.user.auth.data.exceptions

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.user.auth.R

internal class IncorrectPinCodeException : Exception(), LocalizableResource {

    override val resourceString =
        StringResource(R.string.fragment_feature_user_auth_cashier_error_incorrect_pin_code_message)
}