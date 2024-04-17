package  uz.uzkassa.smartpos.core.data.source.resource.user.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.database.SupportRoomDatabase
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserRelation
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRoleEntity

@Dao
abstract class UserRelationDao(private val database: SupportRoomDatabase) : BaseDao {
    private val userRoleEntities: List<UserRoleEntity> by lazy {
        database.getDao<UserRoleEntityDao>().getEntities()
    }

    @Query(value = "SELECT * FROM users WHERE CASE WHEN user_is_owner = 0 THEN user_branch_id = :branchId ELSE user_is_owner = 1 END AND user_is_dismissed = 0")
    internal abstract fun getUserRelationsByBranchId(branchId: Long): List<UserRelation>

    @Query(value = "SELECT * FROM users WHERE CASE WHEN user_is_owner = 0 THEN user_branch_id = :branchId ELSE user_is_owner = 1 END AND user_is_dismissed = 0")
    internal abstract fun getUserRelationsFlowByBranchId(branchId: Long): Flow<List<UserRelation>>

    @Query(value = "SELECT * FROM users WHERE user_is_dismissed = 0")
    internal abstract fun getUserRelations(): List<UserRelation>

    @Query(value = "SELECT * FROM users WHERE user_is_dismissed = 0")
    internal abstract fun getUserRelationsFlow(): Flow<List<UserRelation>>

    @Query(value = "SELECT * FROM users WHERE user_id = :userId AND user_is_dismissed = 0")
    internal abstract fun getUserRelationByUserId(userId: Long): UserRelation

    @Query(value = "SELECT * FROM users WHERE user_id = :userId AND user_is_dismissed = 0")
    internal abstract fun getUserRelationFlowByUserId(userId: Long): Flow<UserRelation>

    fun getRelationsByBranchId(branchId: Long): List<UserRelation> {
        return getUserRelationsByBranchId(branchId)
            .map { setUserRoleEntities(it) }
    }

    fun getRelationsFlowByBranchId(branchId: Long): Flow<List<UserRelation>> {
        return getUserRelationsFlowByBranchId(branchId)
            .map { setUserRoleEntities(it) }
    }

    fun getRelationsFlow(): Flow<List<UserRelation>> {
        return getUserRelationsFlow()
            .map { setUserRoleEntities(it) }
    }

    fun getRelationByUserId(userId: Long): UserRelation {
        val relation: UserRelation = getUserRelationByUserId(userId)
        return setUserRoleEntities(relation)
    }

    fun getRelationFlowByUserId(userId: Long): Flow<UserRelation> {
        return getUserRelationFlowByUserId(userId)
            .map { setUserRoleEntities(it) }
    }

    fun clearAllTables(){
        database.clearAllTables()
        database.clearAll()
    }


    private fun setUserRoleEntities(list: List<UserRelation>): List<UserRelation> {
        return list.map { setUserRoleEntities(it) }
    }

    private fun setUserRoleEntities(relation: UserRelation): UserRelation {
        val entities: List<UserRoleEntity> =
            userRoleEntities.filter { relation.userEntity.userRoleCodes.contains(it.code) }
        return relation.copy(userRoleEntities = entities)
    }
}