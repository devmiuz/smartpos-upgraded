package uz.uzkassa.smartpos.core.data.source.resource.user.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameEntity
import uz.uzkassa.smartpos.core.data.source.resource.language.model.LanguageEntity

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val id: Long,

    @ColumnInfo(name = "user_branch_id")
    val branchId: Long,

    @ColumnInfo(name = "user_phone_number")
    val phoneNumber: String,

    @Embedded(prefix = "user_")
    val language: LanguageEntity,

    @ColumnInfo(name = "user_is_activated")
    val isActivated: Boolean,

    @ColumnInfo(name = "user_is_blocked")
    val isBlocked: Boolean,

    @ColumnInfo(name = "user_is_owner")
    val isOwner: Boolean,

    @Embedded(prefix = "user_")
    val fullName: FullNameEntity,

    @ColumnInfo(name = "user_user_role_code")
    val userRoleCodes: Array<String>,

    @ColumnInfo(name = "user_is_dismissed")
    val isDismissed: Boolean
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false
        if (branchId != other.branchId) return false
        if (phoneNumber != other.phoneNumber) return false
        if (language != other.language) return false
        if (isBlocked != other.isBlocked) return false
        if (isOwner != other.isOwner) return false
        if (fullName != other.fullName) return false
        if (!userRoleCodes.contentEquals(other.userRoleCodes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result: Int = id.hashCode()
        result = 31 * result + branchId.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + isBlocked.hashCode()
        result = 31 * result + isOwner.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + userRoleCodes.contentHashCode()
        return result
    }
}