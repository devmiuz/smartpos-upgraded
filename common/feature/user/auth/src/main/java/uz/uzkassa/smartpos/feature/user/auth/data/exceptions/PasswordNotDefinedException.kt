package uz.uzkassa.smartpos.feature.user.auth.data.exceptions

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.user.auth.R

internal class PasswordNotDefinedException : Exception(), LocalizableResource {

    override val resourceString =
        StringResource(R.string.fragment_feature_user_auth_supervisor_error_password_not_inputted_message)
}