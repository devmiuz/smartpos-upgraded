package uz.uzkassa.smartpos.feature.activitytype.selection.domain

import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ChildActivityTypeBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.HasChildActivityTypesBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.channel.ParentActivityTypeIdBroadcastChannel
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.data.repository.ActivityTypeRepository
import javax.inject.Inject

internal class ActivityTypeSelectionInteractor @Inject constructor(
    private val activityTypeRepository: ActivityTypeRepository,
    private val hasChildActivityTypesBroadcastChannel: HasChildActivityTypesBroadcastChannel,
    private val childActivityTypeBroadcastChannel: ChildActivityTypeBroadcastChannel,
    private val parentActivityTypeIdBroadcastChannel: ParentActivityTypeIdBroadcastChannel
) {
    private val activityTypes: MutableList<ActivityType> =
        activityTypeRepository.getSelectedActivityTypes().toMutableList()

    private var selectedParentId: Long? = null

    init {
        hasChildActivityTypesBroadcastChannel.sendBlocking(activityTypes.isNotEmpty())
    }

    fun getChildActivityTypes(): List<ActivityType> =
        activityTypes

    fun setParentActivityType(activityType: ActivityType) {
        selectedParentId = activityType.parentId
        parentActivityTypeIdBroadcastChannel.sendBlocking(activityType.id)
    }

    fun setChildActivityType(childActivityType: ChildActivityType) {
        selectedParentId = childActivityType.activityType.parentId
        activityTypeRepository.setChildActivityType(childActivityType)

        if (childActivityType.isSelected && !activityTypeRepository.isMultiSelection())
            activityTypes.firstOrNull()?.let {
                sendChildActivityType(ChildActivityType(it, false))
                return@let activityTypes.remove(it)
            }

        if (childActivityType.isSelected && !activityTypes.contains(childActivityType.activityType))
            activityTypes.add(childActivityType.activityType)
        else activityTypes.remove(childActivityType.activityType)

        hasChildActivityTypesBroadcastChannel.sendBlocking(activityTypes.isNotEmpty())

        val isSelected: Boolean = childActivityType.isSelected
        sendChildActivityType(childActivityType.copy(isSelected = isSelected))
    }

    private fun sendChildActivityType(childActivityType: ChildActivityType) {
        if (selectedParentId == null) return
        if (selectedParentId == childActivityType.activityType.parentId)
            childActivityTypeBroadcastChannel.sendBlocking(childActivityType)
    }
}