package uz.uzkassa.smartpos.feature.activitytype.selection.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType

internal interface ActivityTypeRepository {

    fun getParentActivityTypes(): Flow<List<ActivityType>>

    fun getChildActivityTypesByParentId(parentId: Long): Flow<List<ChildActivityType>>

    fun getSelectedActivityTypes(): List<ActivityType>

    fun isMultiSelection(): Boolean

    fun setChildActivityType(childActivityType: ChildActivityType)
}