package uz.uzkassa.smartpos.core.data.source.resource.activitytype.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeResponse

interface ActivityTypeRestService {

    fun getActivityTypes(): Flow<List<ActivityTypeResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): ActivityTypeRestService =
            ActivityTypeRestServiceImpl(retrofit.create())
    }
}