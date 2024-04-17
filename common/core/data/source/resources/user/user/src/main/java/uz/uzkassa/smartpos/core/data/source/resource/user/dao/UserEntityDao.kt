package  uz.uzkassa.smartpos.core.data.source.resource.user.dao

import androidx.room.Dao
import androidx.room.Query
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserEntity
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse

@Dao
abstract class UserEntityDao : BaseDao.Impl<UserEntity>() {

    @Query(value = "DELETE FROM users WHERE user_id = :userId")
    abstract fun deleteById(userId: Long)

    @Query(value = "DELETE FROM users WHERE user_branch_id = :branchId AND user_id NOT IN (SELECT user_id FROM users WHERE user_id IN (:userIds))")
    abstract fun deleteAllExceptUserIds(userIds: LongArray, branchId: Long)

    @Query(value = "DELETE FROM users")
    abstract fun deleteAll()

    fun save(response: UserResponse) {
        insertOrUpdate(response.mapToEntity())
    }

    fun save(responses: List<UserResponse>) {
        responses.groupBy { it.branchId }
            .onEach { it ->
                val branchId: Long = it.key
                val userIds: LongArray = it.value.map { it.id }.toLongArray()
                deleteAllExceptUserIds(userIds, branchId)
            }
        insertOrUpdate(responses.mapToEntities())
    }
}