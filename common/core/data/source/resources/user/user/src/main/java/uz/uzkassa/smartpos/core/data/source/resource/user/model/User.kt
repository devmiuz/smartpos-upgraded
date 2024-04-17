package uz.uzkassa.smartpos.core.data.source.resource.user.model

import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole

data class User(
    val id: Long,
    val branchId: Long,
    val phoneNumber: String,
    val language: Language,
    val isOwner : Boolean,
    val isDismissed : Boolean,
    val isActivated: Boolean,
    val isBlocked: Boolean,
    val fullName: FullName,
    val userRole: UserRole,
    val userRoles: List<UserRole>,
    val userRoleCodes : Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (branchId != other.branchId) return false
        if (phoneNumber != other.phoneNumber) return false
        if (language != other.language) return false
        if (isOwner != other.isOwner) return false
        if (isDismissed != other.isDismissed) return false
        if (isActivated != other.isActivated) return false
        if (isBlocked != other.isBlocked) return false
        if (fullName != other.fullName) return false
        if (userRole != other.userRole) return false
        if (userRoles != other.userRoles) return false
        if (!userRoleCodes.contentEquals(other.userRoleCodes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + branchId.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + isOwner.hashCode()
        result = 31 * result + isDismissed.hashCode()
        result = 31 * result + isActivated.hashCode()
        result = 31 * result + isBlocked.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + userRole.hashCode()
        result = 31 * result + userRoles.hashCode()
        result = 31 * result + userRoleCodes.contentHashCode()
        return result
    }
}