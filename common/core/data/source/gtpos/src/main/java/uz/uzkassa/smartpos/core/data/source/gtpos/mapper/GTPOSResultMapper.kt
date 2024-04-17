package uz.uzkassa.smartpos.core.data.source.gtpos.mapper

import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType.UNKNOWN
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType.UNKNOWN_RESULT_ERROR
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Response
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Status.ERROR
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Type.BATCH_CLOSE
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Type.BATCH_REPORT
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTConnectResult
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult.Companion.error
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult.Companion.success

internal fun GTConnectResult?.mapToResult(): GTPOSResult<Response> =
    when (this) {
        null -> error(UNKNOWN)
        else -> when (status) {
            ERROR -> {
                val errorType: GTPOSErrorType =
                    if (resultCode != null && resultText != null)
                        UNKNOWN_RESULT_ERROR(resultCode, resultText)
                    else UNKNOWN
                error(errorType)
            }
            else -> when (operation) {
                BATCH_CLOSE, BATCH_REPORT -> success(Response.Batch(operation, status))
                else -> {
                    val response: Response.Payment =
                        Response.Payment(
                            amount = checkNotNull(amount),
                            commissionAmount = commissionAmount,
                            currency = checkNotNull(currency),
                            operation = checkNotNull(operation),
                            status = status
                        )
                    success(response)
                }
            }
        }
    }