package uz.uzkassa.smartpos.core.data.source.resource.rest.response

import java.io.Serializable

data class RestErrorResponse(
    val title: String?,
    val detail: String?,
    val message: String?,
    override val httpErrorCode: Int
) : HttpErrorResponse, Serializable