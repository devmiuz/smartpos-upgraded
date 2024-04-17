package uz.uzkassa.smartpos.core.data.source.fiscal.exception

import uz.uzkassa.cto.fiscal.integration.model.error.FiscalError
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import java.io.Serializable

data class FiscalSourceException internal constructor(
    val fiscalErrorType: FiscalErrorType,
    override val resourceString: ResourceString = fiscalErrorType.resourceString
) : RuntimeException(), LocalizableResource, Serializable {

    internal constructor(fiscalError: FiscalError) : this(FiscalErrorType.valueOf(fiscalError))
}