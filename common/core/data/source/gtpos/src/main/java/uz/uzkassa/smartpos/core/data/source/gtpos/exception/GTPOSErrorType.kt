package uz.uzkassa.smartpos.core.data.source.gtpos.exception

import uz.uzkassa.smartpos.core.data.source.gtpos.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import java.io.Serializable

@Suppress("ClassName")
sealed class GTPOSErrorType : Serializable, LocalizableResource {

    object APP_NOT_DEFINED : GTPOSErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_gtpos_error_app_not_defined)
    }

    object UPDATE_REQUIRED : GTPOSErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_gtpos_error_update_required)
    }

    class UNKNOWN_RESULT_ERROR internal constructor(
        val resultCode: String,
        resultText: String
    ) : GTPOSErrorType() {
        override val resourceString: ResourceString =
            StringResource(
                R.string.data_source_gtpos_error_gtpos_result_error,
                resultText,
                resultCode
            )
    }

    object UNKNOWN : GTPOSErrorType() {
        override val resourceString: ResourceString =
            StringResource(R.string.data_source_gtpos_error_unknown)
    }
}