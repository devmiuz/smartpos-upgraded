package uz.uzkassa.smartpos.core.data.source.resource.user.model

import androidx.room.Embedded
import androidx.room.Ignore
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleEntity

data class UserRelation @Ignore internal constructor(
    @Embedded
    val userEntity: UserEntity,

    @Ignore
    val userRoleEntities: List<UserRoleEntity>
) {

    constructor(userEntity: UserEntity) : this(userEntity, emptyList())
}