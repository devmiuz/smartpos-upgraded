package uz.uzkassa.smartpos.feature.user.saving.data.model.update

import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

data class UserUpdateData internal constructor(
    val branch: Branch,
    val branches: List<Branch>,
    val user: User,
    val userRoles: List<UserRole>
)