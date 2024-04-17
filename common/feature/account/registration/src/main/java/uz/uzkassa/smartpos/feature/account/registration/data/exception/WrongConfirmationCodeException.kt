package uz.uzkassa.smartpos.feature.account.registration.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.account.registration.R

internal class WrongConfirmationCodeException : Exception(), LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_account_registration_confirmation_code_error_wrong_confirmation_code_title)
}