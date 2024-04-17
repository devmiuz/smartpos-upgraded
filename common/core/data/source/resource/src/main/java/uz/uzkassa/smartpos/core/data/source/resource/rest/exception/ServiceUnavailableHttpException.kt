package uz.uzkassa.smartpos.core.data.source.resource.rest.exception

import uz.uzkassa.smartpos.core.data.source.resource.R
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import java.io.Serializable

class ServiceUnavailableHttpException internal constructor(
    override val response: HttpErrorResponse
) : HttpException(response), Serializable {

    override val resourceString: ResourceString =
        StringResource(R.string.data_source_http_error_service_unavailable)
}
