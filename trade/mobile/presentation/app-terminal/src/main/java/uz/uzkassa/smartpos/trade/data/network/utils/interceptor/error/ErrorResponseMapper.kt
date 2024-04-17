package uz.uzkassa.smartpos.trade.data.network.utils.interceptor.error

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.RestErrorResponse
import uz.uzkassa.smartpos.core.manager.logger.Logger
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.error.HttpErrorResponseMapper

internal class ErrorResponseMapper : HttpErrorResponseMapper {

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val json: Json = Json.actual

    override fun onHandleResponse(httpErrorCode: Int, response: String?): HttpErrorResponse? {
        if (response == null) return null
        val jsonObject: JsonObject =
            runCatching { json.parseJson(response).jsonObject }.getOrNull() ?: return null

        Logger.e("Error response", jsonObject.toString())

        val title: String? = jsonObject["title"]?.primitive?.contentOrNull
        val detail: String? = jsonObject["detail"]?.primitive?.contentOrNull
        val message: String? = jsonObject["message"]?.primitive?.contentOrNull
        return RestErrorResponse(title, detail, message, httpErrorCode)
    }
}