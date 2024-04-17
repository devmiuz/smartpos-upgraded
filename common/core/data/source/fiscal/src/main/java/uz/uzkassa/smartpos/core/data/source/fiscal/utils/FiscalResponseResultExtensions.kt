package uz.uzkassa.smartpos.core.data.source.fiscal.utils

import uz.uzkassa.cto.fiscal.integration.model.FiscalResponseResult
import uz.uzkassa.cto.fiscal.integration.model.error.FiscalError
import uz.uzkassa.smartpos.core.data.source.fiscal.exception.FiscalSourceException

internal fun <T : Any> FiscalResponseResult<T>.dataOrError(): T? {
    val fiscalError: FiscalError? = errorOrNull() ?: return dataOrThrow()
    return if (isSuccess) dataOrThrow()
    else throw FiscalSourceException(checkNotNull(fiscalError))
}