package uz.uzkassa.smartpos.core.data.manager.device.exception

import uz.uzkassa.engine.device.integration.common.error.EngineError
import uz.uzkassa.smartpos.core.data.manager.device.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import java.io.Serializable

@Suppress("ClassName")
sealed class DeviceInfoErrorType : Serializable, LocalizableResource {

    object LOCATION_GPS_IS_TURNING_OFF : DeviceInfoErrorType() {
        override val resourceString =
            StringResource(R.string.data_source_device_info_manager_error_location_gps_is_turning_off)
    }

    object LOCATION_BAD_NETWORK : DeviceInfoErrorType() {
        override val resourceString =
            StringResource(R.string.data_source_device_info_manager_error_location_bad_network)
    }

    object LOCATION_NOT_FOUND : DeviceInfoErrorType() {
        override val resourceString =
            StringResource(R.string.data_source_device_info_manager_error_location_not_found)
    }

    object APP_NOT_DEFINED : DeviceInfoErrorType() {
        override val resourceString =
            StringResource(R.string.data_source_device_info_manager_error_app_not_defined)
    }

    object VERSION_NOT_ACTUAL : DeviceInfoErrorType() {
        override val resourceString =
            StringResource(R.string.data_source_device_info_manager_error_version_not_actual)
    }

    object UNKNOWN : DeviceInfoErrorType() {
        override val resourceString =
            StringResource(R.string.data_source_device_info_manager_error_unknown)
    }

    internal companion object {

        fun valueOf(engineError: EngineError): DeviceInfoErrorType = when (engineError) {
            EngineError.LOCATION_GPS_IS_TURN_OFF -> LOCATION_GPS_IS_TURNING_OFF
            EngineError.LOCATION_BAD_NETWORK -> LOCATION_BAD_NETWORK
            EngineError.LOCATION_NOT_FOUND -> LOCATION_NOT_FOUND
            EngineError.APP_NOT_DEFINED -> APP_NOT_DEFINED
            EngineError.VERSION_NOT_ACTUAL -> VERSION_NOT_ACTUAL
            else -> UNKNOWN
        }
    }
}