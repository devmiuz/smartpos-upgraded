package uz.uzkassa.smartpos.core.data.source.resource.activitytype.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse

internal interface ActivityTypeRestServiceInternal {

    @GET(API_ACTIVITY_TYPES)
    fun getActivityTypes(): Flow<List<ActivityTypeResponse>>

    private companion object {
        const val API_ACTIVITY_TYPES: String = "api/activity-types"
    }
}