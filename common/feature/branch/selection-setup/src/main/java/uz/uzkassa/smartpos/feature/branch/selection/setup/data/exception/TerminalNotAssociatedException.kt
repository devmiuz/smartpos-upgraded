package uz.uzkassa.smartpos.feature.branch.selection.setup.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.branch.selection.setup.R
import java.io.Serializable

internal class TerminalNotAssociatedException : RuntimeException(), LocalizableResource,
    Serializable {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_branch_selection_setup_error_terminal_not_associated)
}