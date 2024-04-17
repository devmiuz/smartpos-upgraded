package  uz.uzkassa.smartpos.core.data.source.resource.user.role.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleEntity

@Dao
abstract class UserRoleEntityDao : BaseDao.Impl<UserRoleEntity>() {

    @Query(value = "SELECT * FROM user_roles")
    abstract fun getEntities(): List<UserRoleEntity>
}