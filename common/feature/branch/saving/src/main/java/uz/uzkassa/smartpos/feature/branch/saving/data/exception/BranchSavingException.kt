package uz.uzkassa.smartpos.feature.branch.saving.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.branch.saving.R

internal data class BranchSavingException(
    val isActivityTypeNotDefined: Boolean,
    val isNameNotDefined: Boolean,
    val isRegionNotDefined: Boolean,
    val isCityNotDefined: Boolean,
    val isAddressNotDefined: Boolean
) : Exception(), LocalizableResource {

    val isPassed: Boolean
        get() = isActivityTypeNotDefined || isNameNotDefined ||
                isRegionNotDefined || isCityNotDefined || isAddressNotDefined

    override val resourceString: ResourceString
        get() {
            val resourceId: Int = when {
                isActivityTypeNotDefined ->
                    R.string.fragment_feature_branch_saving_error_activity_type_selection
                isNameNotDefined ->
                    R.string.fragment_feature_branch_saving_error_branch_name_input
                isRegionNotDefined ->
                    R.string.fragment_feature_branch_saving_error_region_selection
                isCityNotDefined ->
                    R.string.fragment_feature_branch_saving_error_city_selection
                isAddressNotDefined ->
                    R.string.fragment_feature_branch_saving_error_address_input
                else -> R.string.core_presentation_common_error_unknown_message
            }

            return StringResource(resourceId)
        }
}