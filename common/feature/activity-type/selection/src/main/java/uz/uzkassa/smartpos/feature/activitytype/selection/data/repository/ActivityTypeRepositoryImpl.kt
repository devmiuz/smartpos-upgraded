package uz.uzkassa.smartpos.feature.activitytype.selection.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.service.ActivityTypeRestService
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureArgs
import javax.inject.Inject

internal class ActivityTypeRepositoryImpl @Inject constructor(
    private val activityTypeEntityDao: ActivityTypeEntityDao,
    private val activityTypeRestService: ActivityTypeRestService,
    private val activityTypeSelectionFeatureArgs: ActivityTypeSelectionFeatureArgs
) : ActivityTypeRepository {
    private val activityTypes: MutableList<ActivityType> = arrayListOf()
    private val childActivityTypes: MutableList<ActivityType> =
        activityTypeSelectionFeatureArgs.activityTypes.toMutableList()

    @FlowPreview
    override fun getParentActivityTypes(): Flow<List<ActivityType>> {
        return activityTypeRestService.getActivityTypes()
            .onEach { activityTypeEntityDao.upsert(it.mapToEntities()) }
            .map { it.map() }
            .onEach { activityTypes.apply { clear(); addAll(it) } }
            .map { it -> it.filter { it.parentId == null } }
    }

    override fun getChildActivityTypesByParentId(parentId: Long): Flow<List<ChildActivityType>> {
        return flow { emit(map(activityTypes.filter { it.parentId == parentId })) }
    }

    override fun getSelectedActivityTypes(): List<ActivityType> =
        childActivityTypes

    override fun isMultiSelection(): Boolean =
        activityTypeSelectionFeatureArgs.isMultiSelection

    override fun setChildActivityType(childActivityType: ChildActivityType) {
        if (childActivityType.isSelected && !isMultiSelection())
            childActivityTypes.firstOrNull()?.let { childActivityTypes.remove(it) }

        if (childActivityType.isSelected) childActivityTypes.add(childActivityType.activityType)
        else childActivityTypes.remove(childActivityType.activityType)
    }

    private fun map(list: List<ActivityType>): List<ChildActivityType> {
        val result: MutableList<ChildActivityType> = arrayListOf()

        list.forEach { type ->
            val activityType: ActivityType? = childActivityTypes.find { type.id == it.id }
            val activityTypeSelection = ChildActivityType(
                activityType = activityType ?: type,
                isSelected = activityType != null
            )

            result.add(activityTypeSelection)
        }
        return result
    }
}