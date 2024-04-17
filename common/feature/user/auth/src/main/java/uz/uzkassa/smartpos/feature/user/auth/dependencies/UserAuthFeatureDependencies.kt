package uz.uzkassa.smartpos.feature.user.auth.dependencies

import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.service.ShiftReportRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface UserAuthFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val fiscalShiftOperationSource: FiscalShiftSource

    val shiftReportEntityDao: ShiftReportEntityDao

    val shiftReportService: ShiftReportRestService

    val userAuthFeatureArgs: UserAuthFeatureArgs

    val userAuthFeatureCallback: UserAuthFeatureCallback

    val userAuthRestService: UserAuthRestService

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService
}