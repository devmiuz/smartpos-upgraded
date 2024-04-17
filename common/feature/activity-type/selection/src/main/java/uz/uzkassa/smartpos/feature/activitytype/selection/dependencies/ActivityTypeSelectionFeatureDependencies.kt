package uz.uzkassa.smartpos.feature.activitytype.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.service.ActivityTypeRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ActivityTypeSelectionFeatureDependencies {

    val activityTypeEntityDao: ActivityTypeEntityDao

    val activityTypeRestService: ActivityTypeRestService

    val activityTypeSelectionFeatureCallback: ActivityTypeSelectionFeatureCallback

    val activityTypeSelectionFeatureArgs: ActivityTypeSelectionFeatureArgs

    val coroutineContextManager: CoroutineContextManager
}