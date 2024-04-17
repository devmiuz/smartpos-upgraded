package uz.uzkassa.smartpos.core.data.manager.printer.exception

import uz.uzkassa.engine.printer.integration.response.status.PrinterStatus
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import java.io.Serializable

data class PrinterException internal constructor(
    val printerErrorType: PrinterErrorType,
    override val resourceString: ResourceString = printerErrorType.resourceString
) : RuntimeException(), LocalizableResource, Serializable {

    internal constructor(printerStatus: PrinterStatus) : this(PrinterErrorType.valueOf(printerStatus))
}