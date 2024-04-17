package uz.uzkassa.smartpos.feature.company.saving.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.company.saving.R

internal class CompanyCreationException(
    val isActivityTypesNotDefined: Boolean,
    val isOwnerLastNameNotDefined: Boolean,
    val isOwnerFirstNameNotDefined: Boolean,
    val isCompanyBusinessTypeNotDefined: Boolean,
    val isNameNotDefined: Boolean,
    val isAddressNotDefined: Boolean,
    val isRegionNotDefined: Boolean,
    val isCityNotDefined: Boolean
) : Exception(), LocalizableResource {

    val isPassed: Boolean
        get() = isActivityTypesNotDefined || isOwnerLastNameNotDefined ||
                isOwnerFirstNameNotDefined || isCompanyBusinessTypeNotDefined ||
                isNameNotDefined || isAddressNotDefined || isRegionNotDefined ||
                isCityNotDefined

    override val resourceString: ResourceString
        get() {
            val resourceId: Int = when {
                isActivityTypesNotDefined ->
                    R.string.fragment_feature_company_saving_error_activity_type_selection
                isOwnerLastNameNotDefined ->
                    R.string.fragment_feature_company_saving_error_surname_input
                isOwnerFirstNameNotDefined ->
                    R.string.fragment_feature_company_saving_error_name_input
                isCompanyBusinessTypeNotDefined ->
                    R.string.fragment_feature_company_saving_error_business_type_input
                isNameNotDefined ->
                    R.string.fragment_feature_company_saving_error_company_name_input
                isAddressNotDefined ->
                    R.string.fragment_feature_company_saving_error_address_input
                isRegionNotDefined ->
                    R.string.fragment_feature_company_saving_error_region_selection
                isCityNotDefined ->
                    R.string.fragment_feature_company_saving_error_city_selection
                else -> R.string.core_presentation_common_error_unknown_message
            }

            return StringResource(resourceId)
        }
}