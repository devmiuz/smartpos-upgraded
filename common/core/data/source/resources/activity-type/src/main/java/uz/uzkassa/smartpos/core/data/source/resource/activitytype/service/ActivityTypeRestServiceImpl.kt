package uz.uzkassa.smartpos.core.data.source.resource.activitytype.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse

internal class ActivityTypeRestServiceImpl(
    private val restServiceInternal: ActivityTypeRestServiceInternal
) : ActivityTypeRestService {

    override fun getActivityTypes(): Flow<List<ActivityTypeResponse>> {
        return restServiceInternal.getActivityTypes()
    }
}