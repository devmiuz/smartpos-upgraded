package uz.uzkassa.smartpos.feature.user.auth.data.repository.shift

import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportEntity
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportResponse
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.service.ShiftReportRestService
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
class ShiftReportRepositoryImpl @Inject constructor(
    private val fiscalShiftSource: FiscalShiftSource,
    private val shiftReportEntityDao: ShiftReportEntityDao,
    private val shiftReportRestService: ShiftReportRestService
) : ShiftReportRepository {
    private val json: Json = Json.actual

    override fun openShiftReport(userId: Long, userName: String): Flow<Unit> {
        return flow { emit(shiftReportEntityDao.getNotSyncedEntity()) }
            .flatMapConcat { entity ->
                return@flatMapConcat if (entity?.finishDate != null)
                    shiftReportRestService
                        .createShiftReport(
                            json.toJson(
                                serializer = ShiftReportResponse.serializer(),
                                value = entity.mapToResponse()
                            )
                        )
                        .onEach { shiftReportEntityDao.updateSyncState() }
                else flowOf(Unit)
            }
            .flatMapConcat { getFiscalOpenShiftResult(userId, userName) }
    }

    private fun getFiscalOpenShiftResult(
        userId: Long,
        userName: String
    ): Flow<Unit> {
        return fiscalShiftSource.openShift(userId, userName)
            .onEach {
                if (it == null) return@onEach
                val entity: ShiftReportEntity? =
                    shiftReportEntityDao.getOpenShiftReportEntity().first()

                if (entity?.fiscalShiftNumber == it.shiftNumber && entity.startDate == it.openDate) return@onEach
                else shiftReportEntityDao.upsert(
                    ShiftReportEntity(
                        startDate = it.openDate,
                        fiscalShiftNumber = it.shiftNumber,
                        userId = userId
                    )
                )
            }
            .map { Unit }
    }
}