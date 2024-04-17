package uz.uzkassa.smartpos.feature.branch.list.data.model.branch

import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch

data class BranchWrapper internal constructor(val branch: Branch, val isCurrent: Boolean)