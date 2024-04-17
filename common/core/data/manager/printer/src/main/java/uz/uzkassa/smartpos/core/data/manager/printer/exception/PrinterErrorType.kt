package uz.uzkassa.smartpos.core.data.manager.printer.exception

import uz.uzkassa.engine.printer.integration.response.status.PrinterStatus
import uz.uzkassa.smartpos.core.data.manager.printer.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import java.io.Serializable

@Suppress("ClassName")
sealed class PrinterErrorType : Serializable, LocalizableResource {

    object APP_NOT_DEFINED : PrinterErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_printer_manager_error_app_not_defined)
    }

    object IS_NO_PAPER : PrinterErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_printer_manager_error_is_no_paper)
    }

    object NOT_INITIALIZED : PrinterErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_printer_manager_error_not_initialized)
    }

    object VERSION_NOT_ACTUAL : PrinterErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_printer_manager_error_version_not_actual)
    }

    object UNKNOWN : PrinterErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_printer_manager_error_unknown)
    }

    internal companion object {

        fun valueOf(printerStatus: PrinterStatus): PrinterErrorType = when (printerStatus) {
            PrinterStatus.IS_NO_PAPER_IN_THE_PRINTER -> IS_NO_PAPER
            PrinterStatus.NOT_EXIST_INITIALIZED_PRINTER -> NOT_INITIALIZED
            PrinterStatus.APP_NOT_DEFINED -> APP_NOT_DEFINED
            PrinterStatus.VERSION_NOT_ACTUAL -> VERSION_NOT_ACTUAL
            else -> UNKNOWN
        }
    }
}