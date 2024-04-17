package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.shift

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.fiscal.model.shift.FiscalCloseShiftResult
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType.APP_NOT_DEFINED
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType.UNKNOWN_RESULT_ERROR
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType.UPDATE_REQUIRED
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSException
import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSource
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.mapper.mapToResponse
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportEntity
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.model.ShiftReportResponse
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.service.ShiftReportRestService
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.core.utils.math.div
import uz.uzkassa.smartpos.core.utils.primitives.roundToBigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class ShiftReportRepositoryImpl @Inject constructor(
    private val gtposBatchSource: GTPOSBatchSource,
    private val fiscalShiftSource: FiscalShiftSource,
    private val shiftReportEntityDao: ShiftReportEntityDao,
    private val shiftReportRestService: ShiftReportRestService
) : ShiftReportRepository {
    private val json: Json = Json.actual
    private var isGTPOSBatchClosed: Boolean = false
    private var lastFiscalCloseShiftResult: FiscalCloseShiftResult? = null

    @FlowPreview
    override fun closeShift(userId: Long): Flow<FiscalCloseShiftResult?> {
        return getFiscalCloseShiftResult(userId)
            .flatMapConcat { result -> closeGTPOSBatch().map { result } }
    }

    override fun pauseShift(): Flow<Unit> {
        return fiscalShiftSource.pauseShift()
    }

    @Suppress("LABEL_NAME_CLASH")
    @FlowPreview
    private fun getFiscalCloseShiftResult(userId: Long): Flow<FiscalCloseShiftResult?> {
        return flowOf(lastFiscalCloseShiftResult)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null) flowOf(it)
                else fiscalShiftSource.closeShift()
                    .onEach { lastFiscalCloseShiftResult = it }
                    .flatMapConcat { result ->
                        return@flatMapConcat if (result != null)
                            createShiftReport(result, userId).map { result }
                        else flowOf(null)
                    }
            }
    }

    private fun createShiftReport(
        result: FiscalCloseShiftResult,
        userId: Long
    ): Flow<Unit> {
        return flowOf(result)
            .onEach {
                shiftReportEntityDao.updateNotSyncedEntity(
                    startDate = it.startDate,
                    finishDate = it.finishDate,
                    fiscalShiftNumber = it.shiftNumber,
                    totalRefundVAT = (it.totalRefundVAT / 100.0).roundToBigDecimal(),
                    totalRefundCard = (it.totalRefundCard / 100.0).roundToBigDecimal(),
                    totalRefundCash = (it.totalRefundCash / 100.0).roundToBigDecimal(),
                    totalRefundCount = it.totalRefundCount,
                    totalSaleVAT = (it.totalSaleVAT / 100.0).roundToBigDecimal(),
                    totalSaleCard = (it.totalSaleCard / 100.0).roundToBigDecimal(),
                    totalSaleCash = (it.totalSaleCash / 100.0).roundToBigDecimal(),
                    totalSaleCount = it.totalSaleCount,
                    userId = userId
                )
            }

            .flatMapConcat {
                val entity: ShiftReportEntity =
                    shiftReportEntityDao.getNotSyncedEntity()
                        ?: return@flatMapConcat flowOf(null)

                val jsonElement: JsonElement =
                    json.toJson(ShiftReportResponse.serializer(), entity.mapToResponse())

                return@flatMapConcat shiftReportRestService
                    .createShiftReport(jsonElement)
                    .onEach { shiftReportEntityDao.updateSyncState() }
            }
            .map { Unit }
            .catch { emit(Unit) }
    }

    private fun closeGTPOSBatch(): Flow<Unit> {
        return if (isGTPOSBatchClosed) flowOf(Unit)
        else gtposBatchSource.batchClose()
            .flatMapConcat {
                return@flatMapConcat if (it.isSuccess) flowOf(Unit)
                else {
                    val exception: GTPOSException = it.errorOrUnknown()
                    when (val errorType: GTPOSErrorType = exception.gtposErrorType) {
                        is APP_NOT_DEFINED, UPDATE_REQUIRED -> flowOf(Unit)
                        is UNKNOWN_RESULT_ERROR -> {
                            when (errorType.resultCode.toIntOrNull()) {
                                -16, -81, -92 -> flowOf(Unit)
                                else -> throw exception
                            }
                        }
                        else -> throw exception
                    }
                }
            }
            .onEach {
                isGTPOSBatchClosed = true
                delay(TimeUnit.SECONDS.toMillis(1))
            }
    }
}