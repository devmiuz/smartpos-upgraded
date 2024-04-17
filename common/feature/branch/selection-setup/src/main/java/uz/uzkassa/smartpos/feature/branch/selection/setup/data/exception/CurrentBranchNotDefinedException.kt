package uz.uzkassa.smartpos.feature.branch.selection.setup.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.branch.selection.setup.R

internal class CurrentBranchNotDefinedException : RuntimeException(), LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_branch_selection_setup_error_branch_not_selected)
}