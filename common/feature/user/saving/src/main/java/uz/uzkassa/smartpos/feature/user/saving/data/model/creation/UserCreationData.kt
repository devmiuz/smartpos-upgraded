package uz.uzkassa.smartpos.feature.user.saving.data.model.creation

import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

data class UserCreationData internal constructor(
    val branches: List<Branch>,
    val userRoles: List<UserRole>
)