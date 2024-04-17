package uz.uzkassa.smartpos.feature.activitytype.selection.domain.parent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.activitytype.selection.data.repository.ActivityTypeRepository
import javax.inject.Inject

internal class ActivityTypeParentInteractor @Inject constructor(
    private val activityTypeRepository: ActivityTypeRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getParentActivityTypes(): Flow<Result<List<ActivityType>>> {
        return activityTypeRepository
            .getParentActivityTypes()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}