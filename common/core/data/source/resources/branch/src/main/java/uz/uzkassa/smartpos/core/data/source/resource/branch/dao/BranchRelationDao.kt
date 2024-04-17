package uz.uzkassa.smartpos.core.data.source.resource.branch.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchRelation
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class BranchRelationDao : BaseDao {

    @Query(value = "SELECT * FROM branches LEFT JOIN activity_types ON branch_activity_type_id = activity_type_id LEFT JOIN regions ON branch_region_id = region_id LEFT JOIN cities ON branch_city_id = city_id")
    abstract fun getRelationsFlow(): Flow<List<BranchRelation>>

    @Query(value = "SELECT * FROM branches LEFT JOIN activity_types ON branch_activity_type_id = activity_type_id LEFT JOIN regions ON branch_region_id = region_id LEFT JOIN cities ON branch_city_id = city_id WHERE branch_id = :branchId")
    abstract fun getRelationFlowByBranchId(branchId: Long): Flow<BranchRelation>
}