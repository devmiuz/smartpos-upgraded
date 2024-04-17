package uz.uzkassa.smartpos.feature.user.saving.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.user.saving.R

internal data class UserManageCreationException(
    val isBranchNotDefined: Boolean,
    val isPhoneNumberNotDefined: Boolean,
    val isUserRoleNotDefined: Boolean,
    val isLastNameNotDefined: Boolean,
    val isFirstNameNotDefined: Boolean
) : Exception(), LocalizableResource {

    val isPassed: Boolean
        get() = isBranchNotDefined || isPhoneNumberNotDefined || isUserRoleNotDefined
                || isLastNameNotDefined || isFirstNameNotDefined

    override val resourceString: ResourceString
        get() {
            val resourceId: Int = when {
                isBranchNotDefined ->
                    R.string.fragment_feature_user_saving_error_user_branch_not_selected
                isPhoneNumberNotDefined ->
                    R.string.fragment_feature_user_saving_error_user_phone_number_not_selected
                isUserRoleNotDefined ->
                    R.string.fragment_feature_user_saving_error_user_role_not_selected
                isLastNameNotDefined ->
                    R.string.fragment_feature_user_saving_error_user_surname_not_inputted
                isFirstNameNotDefined ->
                    R.string.fragment_feature_user_saving_error_user_name_not_inputted

                else -> R.string.core_presentation_common_error_unknown_message
            }
            return StringResource(resourceId)
        }
}