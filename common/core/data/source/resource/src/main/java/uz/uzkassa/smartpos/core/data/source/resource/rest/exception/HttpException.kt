package uz.uzkassa.smartpos.core.data.source.resource.rest.exception

import uz.uzkassa.smartpos.core.data.source.resource.R
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import java.io.IOException
import java.io.Serializable

open class HttpException internal constructor(
    open val response: HttpErrorResponse
) : IOException(), Serializable, LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.data_source_http_error_unknown)
}