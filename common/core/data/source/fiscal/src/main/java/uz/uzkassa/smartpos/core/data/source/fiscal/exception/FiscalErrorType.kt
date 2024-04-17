package uz.uzkassa.smartpos.core.data.source.fiscal.exception

import uz.uzkassa.cto.fiscal.integration.model.error.FiscalError
import uz.uzkassa.smartpos.core.data.source.fiscal.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import java.io.Serializable

@Suppress("ClassName")
sealed class FiscalErrorType : Serializable, LocalizableResource {

    object RECEIPT_COUNT_ZERO : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_count_zero)
    }

    object RECEIPT_INDEX_OUT_OF_BOUNDS : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_index_out_of_bounds)
    }

    object RECEIPT_NOT_FOUND : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_not_found)
    }

    object DATA_SIZE_NOT_SUPPORTED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_data_size_not_supported)
    }

    object RECEIPT_ALREADY_CLOSED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_already_closed)
    }

    object RECEIPT_IS_NOT_CLOSED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_is_not_closed)
    }

    object EXTRA_DATA_PASSED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_extra_data_passed)
    }

    object RECEIPT_FORMAT_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_format_invalid)
    }

    object RECEIPT_TOTAL_PRICE_OVERFLOW : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_total_price_overflow)
    }

    object RECEIPT_TOTAL_PRICE_MISMATCH : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_total_price_mismatch)
    }

    object RECEIPT_PRICE_OVERFLOW : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_price_overflow)
    }

    object RECEIPT_VAT_OVERFLOW : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_vat_overflow)
    }

    object RECEIPT_SEQ_OVERFLOW : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_seq_overflow)
    }

    object RECEIPT_CASH_OVERFLOW : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_cash_overflow)
    }

    object RECEIPT_CARD_OVERFLOW : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_card_overflow)
    }

    object RECEIPT_PARTITION_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_partition_invalid)
    }

    object RECEIPT_MEMORY_FULL : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_memory_full)
    }

    object RECEIPT_TIME_PAST : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_time_past)
    }

    object RECEIPT_STORE_DAYS_LIMIT_EXCEEDED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_store_days_limit_exceeded)
    }

    object LAST_TRANSACTION_TIME_FORMAT_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_last_transaction_time_format_invalid)
    }

    object FIRST_RECEIPT_TRANSACTION_TIME_FORMAT_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_first_receipt_transaction_time_format_invalid)
    }

    object ACKNOWLEDGE_WRONG_LENGTH : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_acknowledge_wrong_length)
    }

    object ACKNOWLEDGE_SIGNATURE_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_acknowledge_signature_invalid)
    }

    object ACKNOWLEDGE_TERMINAL_ID_MISMATCH : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_acknowledge_terminal_id_mismatch)
    }

    object Z_REPORT_PARTITION_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_z_report_partition_invalid)
    }

    object OPEN_CLOSE_Z_REPORT_WRONG_LENGTH : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_open_close_z_report_wrong_length)
    }

    object CLOSE_Z_REPORT_TIME_PAST : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_close_z_report_time_past)
    }

    object Z_REPORT_SPACE_IS_FULL : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_z_report_space_is_full)
    }

    object CURRENT_TIME_FORMAT_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_current_time_format_invalid)
    }

    object RECEIPT_TRANSACTION_TIME_FORMAT_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_first_receipt_transaction_time_format_invalid)
    }

    object Z_REPORT_INDEX_OUT_OF_BOUNDS : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_z_report_index_of_bounds)
    }

    object LOCK_CHALLENGE_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_lock_challenge_invalid)
    }

    object LOCKED_FOREVER : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_locked_forever)
    }

    object CONFIGURE_WRONG_LENGTH : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_configure_wrong_length)
    }

    object CONFIGURE_SIGNATURE_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_configure_signature_invalid)
    }

    object CURRENT_Z_REPORT_IS_EMPTY : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_current_z_report_is_empty)
    }

    object RECEIPT_TOTAL_PRICE_ZERO : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_receipt_total_price_zero)
    }

    object Z_REPORT_IS_NOT_OPEN : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_z_report_is_not_open)
    }

    object Z_REPORT_OPEN_TIME_FORMAT_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_z_report_open_time_format_invalid)
    }

    object SALE_REFUND_COUNT_OVERFLOW : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_sale_refund_count_overflow)
    }

    object Z_REPORT_IS_ALREADY_OPEN : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_z_report_is_already_open)
    }

    object NOT_ENOUGH_CASH_FOR_REFUND : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_not_enough_cash_for_refund)
    }

    object NOT_ENOUGH_CARD_FOR_REFUND : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_not_enough_card_for_refund)
    }

    object NOT_ENOUGH_VAT_FOR_REFUND : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_not_enough_vat_for_refund)
    }

    object OPEN_Z_REPORT_TIME_PAST : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_open_z_report_time_past)
    }

    object MAINTENANCE_REQUIRED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_maintenance_required)
    }

    object CONFIGURE_UNLOCK_KEY_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_configure_unlock_key_invalid)
    }

    object SAM_MODULE_NOT_FOUND : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_sam_module_not_found)
    }

    object SAM_MODULE_NOT_RESPONDING : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_sam_module_not_responding)
    }

    object SAM_MODULE_ERROR : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_sam_module_error)
    }

    object NOT_APPLET_INIT : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_not_applet_init)
    }

    object EXCHANGE : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_exchange)
    }

    object DIFFERENT_DATA : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_different_data)
    }

    object Z_REPORT_NUMBER_IS_INVALID : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_z_report_number_is_invalid)
    }

    object NOT_ACTIVATED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_not_activated)
    }

    @Suppress("SpellCheckingInspection")
    object NOT_FISCALIZED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_not_fiscalized)
    }

    object APP_NOT_DEFINED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_app_not_defined)
    }

    object VERSION_NOT_ACTUAL : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_version_not_actual)
    }

    object UNKNOWN : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_unknown)
    }

    object UNDEFINED_STATE : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_error_unknown)
    }

    object SAM_MODULE_IS_BROKEN : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_sam_module_broken)
    }

    object FORCE_SYNC_IS_NEEDED : FiscalErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_fiscal_force_sync_needed)
    }

    internal companion object {

        fun valueOf(fiscalError: FiscalError): FiscalErrorType = when (fiscalError) {
            FiscalError.RECEIPT_COUNT_ZERO -> RECEIPT_COUNT_ZERO
            FiscalError.RECEIPT_INDEX_OUT_OF_BOUNDS -> RECEIPT_INDEX_OUT_OF_BOUNDS
            FiscalError.RECEIPT_NOT_FOUND -> RECEIPT_NOT_FOUND
            FiscalError.DATA_SIZE_NOT_SUPPORTED -> DATA_SIZE_NOT_SUPPORTED
            FiscalError.RECEIPT_ALREADY_CLOSED -> RECEIPT_ALREADY_CLOSED
            FiscalError.RECEIPT_IS_NOT_CLOSED -> RECEIPT_IS_NOT_CLOSED
            FiscalError.EXTRA_DATA_PASSED -> EXTRA_DATA_PASSED
            FiscalError.RECEIPT_FORMAT_INVALID -> RECEIPT_FORMAT_INVALID
            FiscalError.RECEIPT_TOTAL_PRICE_OVERFLOW -> RECEIPT_TOTAL_PRICE_OVERFLOW
            FiscalError.RECEIPT_TOTAL_PRICE_MISMATCH -> RECEIPT_TOTAL_PRICE_MISMATCH
            FiscalError.RECEIPT_PRICE_OVERFLOW -> RECEIPT_PRICE_OVERFLOW
            FiscalError.RECEIPT_VAT_OVERFLOW -> RECEIPT_VAT_OVERFLOW
            FiscalError.RECEIPT_SEQ_OVERFLOW -> RECEIPT_SEQ_OVERFLOW
            FiscalError.RECEIPT_CASH_OVERFLOW -> RECEIPT_CASH_OVERFLOW
            FiscalError.RECEIPT_CARD_OVERFLOW -> RECEIPT_CARD_OVERFLOW
            FiscalError.RECEIPT_PARTITION_INVALID -> RECEIPT_PARTITION_INVALID
            FiscalError.RECEIPT_MEMORY_FULL -> RECEIPT_MEMORY_FULL
            FiscalError.RECEIPT_TIME_PAST -> RECEIPT_TIME_PAST
            FiscalError.RECEIPT_STORE_DAYS_LIMIT_EXCEEDED -> RECEIPT_STORE_DAYS_LIMIT_EXCEEDED
            FiscalError.LAST_TRANSACTION_TIME_FORMAT_INVALID -> LAST_TRANSACTION_TIME_FORMAT_INVALID
            FiscalError.FIRST_RECEIPT_TRANSACTION_TIME_FORMAT_INVALID -> FIRST_RECEIPT_TRANSACTION_TIME_FORMAT_INVALID
            FiscalError.ACKNOWLEDGE_WRONG_LENGTH -> ACKNOWLEDGE_WRONG_LENGTH
            FiscalError.ACKNOWLEDGE_SIGNATURE_INVALID -> ACKNOWLEDGE_SIGNATURE_INVALID
            FiscalError.ACKNOWLEDGE_TERMINAL_ID_MISMATCH -> ACKNOWLEDGE_TERMINAL_ID_MISMATCH
            FiscalError.Z_REPORT_PARTITION_INVALID -> Z_REPORT_PARTITION_INVALID
            FiscalError.OPEN_CLOSE_Z_REPORT_WRONG_LENGTH -> OPEN_CLOSE_Z_REPORT_WRONG_LENGTH
            FiscalError.CLOSE_Z_REPORT_TIME_PAST -> CLOSE_Z_REPORT_TIME_PAST
            FiscalError.Z_REPORT_SPACE_IS_FULL -> Z_REPORT_SPACE_IS_FULL
            FiscalError.CURRENT_TIME_FORMAT_INVALID -> CURRENT_TIME_FORMAT_INVALID
            FiscalError.RECEIPT_TRANSACTION_TIME_FORMAT_INVALID -> RECEIPT_TRANSACTION_TIME_FORMAT_INVALID
            FiscalError.Z_REPORT_INDEX_OUT_OF_BOUNDS -> Z_REPORT_INDEX_OUT_OF_BOUNDS
            FiscalError.LOCK_CHALLENGE_INVALID -> LOCK_CHALLENGE_INVALID
            FiscalError.LOCKED_FOREVER -> LOCKED_FOREVER
            FiscalError.CONFIGURE_WRONG_LENGTH -> CONFIGURE_WRONG_LENGTH
            FiscalError.CONFIGURE_SIGNATURE_INVALID -> CONFIGURE_SIGNATURE_INVALID
            FiscalError.CURRENT_Z_REPORT_IS_EMPTY -> CURRENT_Z_REPORT_IS_EMPTY
            FiscalError.RECEIPT_TOTAL_PRICE_ZERO -> RECEIPT_TOTAL_PRICE_ZERO
            FiscalError.Z_REPORT_IS_NOT_OPEN -> Z_REPORT_IS_NOT_OPEN
            FiscalError.Z_REPORT_OPEN_TIME_FORMAT_INVALID -> Z_REPORT_OPEN_TIME_FORMAT_INVALID
            FiscalError.SALE_REFUND_COUNT_OVERFLOW -> SALE_REFUND_COUNT_OVERFLOW
            FiscalError.Z_REPORT_IS_ALREADY_OPEN -> Z_REPORT_IS_ALREADY_OPEN
            FiscalError.NOT_ENOUGH_CASH_FOR_REFUND -> NOT_ENOUGH_CASH_FOR_REFUND
            FiscalError.NOT_ENOUGH_CARD_FOR_REFUND -> NOT_ENOUGH_CARD_FOR_REFUND
            FiscalError.NOT_ENOUGH_VAT_FOR_REFUND -> NOT_ENOUGH_VAT_FOR_REFUND
            FiscalError.OPEN_Z_REPORT_TIME_PAST -> OPEN_Z_REPORT_TIME_PAST
            FiscalError.MAINTENANCE_REQUIRED -> MAINTENANCE_REQUIRED
            FiscalError.CONFIGURE_UNLOCK_KEY_INVALID -> CONFIGURE_UNLOCK_KEY_INVALID
            FiscalError.SAM_MODULE_NOT_FOUND -> SAM_MODULE_NOT_FOUND
            FiscalError.SAM_MODULE_NOT_RESPONDING -> SAM_MODULE_NOT_RESPONDING
            FiscalError.SAM_MODULE_IS_BROKEN -> SAM_MODULE_IS_BROKEN
            FiscalError.FORCE_SYNC_IS_NEEDED->FORCE_SYNC_IS_NEEDED
            FiscalError.SAM_MODULE_ERROR -> SAM_MODULE_ERROR
            FiscalError.NOT_APPLET_INIT -> NOT_APPLET_INIT
            FiscalError.EXCHANGE -> EXCHANGE
            FiscalError.DIFFERENT_DATA -> DIFFERENT_DATA
            FiscalError.Z_REPORT_NUMBER_IS_INVALID -> Z_REPORT_NUMBER_IS_INVALID
            FiscalError.NOT_ACTIVATED -> NOT_ACTIVATED
            FiscalError.NOT_FISCALIZED -> NOT_FISCALIZED
            FiscalError.APP_NOT_DEFINED -> APP_NOT_DEFINED
            FiscalError.VERSION_NOT_ACTUAL -> VERSION_NOT_ACTUAL
            FiscalError.UNKNOWN -> UNKNOWN
            FiscalError.UNDEFINED_STATE -> UNDEFINED_STATE
        }
    }
}