package uz.uzkassa.smartpos.feature.activitytype.selection.domain.child

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.activitytype.selection.data.model.ChildActivityType
import uz.uzkassa.smartpos.feature.activitytype.selection.data.repository.ActivityTypeRepository
import javax.inject.Inject

internal class ActivityTypeChildInteractor @Inject constructor(
    private val activityTypeRepository: ActivityTypeRepository,
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getChildActivityTypesByParentId(parentId: Long): Flow<Result<List<ChildActivityType>>> {
        return activityTypeRepository
            .getChildActivityTypesByParentId(parentId)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}