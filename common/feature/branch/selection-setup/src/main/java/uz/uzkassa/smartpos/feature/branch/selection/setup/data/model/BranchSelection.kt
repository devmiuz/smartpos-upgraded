package uz.uzkassa.smartpos.feature.branch.selection.setup.data.model

import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch

data class BranchSelection internal constructor(val branch: Branch, val isSelected: Boolean)