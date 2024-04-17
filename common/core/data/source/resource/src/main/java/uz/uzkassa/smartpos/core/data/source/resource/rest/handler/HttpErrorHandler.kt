package uz.uzkassa.smartpos.core.data.source.resource.rest.handler

import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.*
import uz.uzkassa.smartpos.core.data.source.resource.rest.response.HttpErrorResponse
import java.net.HttpURLConnection

object HttpErrorHandler {

    fun handle(httpErrorCode: Int, errorResponse: HttpErrorResponse): HttpException {
        when (httpErrorCode) {
            HttpURLConnection.HTTP_BAD_REQUEST ->
                throw BadRequestHttpException(errorResponse)
            HttpURLConnection.HTTP_UNAUTHORIZED ->
                throw UnauthorizedHttpException(errorResponse)
            HttpURLConnection.HTTP_PAYMENT_REQUIRED ->
                throw PaymentRequiredHttpException(errorResponse)
            HttpURLConnection.HTTP_FORBIDDEN ->
                throw ForbiddenHttpException(errorResponse)
            HttpURLConnection.HTTP_NOT_FOUND ->
                throw NotFoundHttpException(errorResponse)
            HttpURLConnection.HTTP_INTERNAL_ERROR ->
                throw InternalServerErrorHttpException(errorResponse)
            HttpURLConnection.HTTP_UNAVAILABLE ->
                throw ServiceUnavailableHttpException(errorResponse)
            else -> throw HttpException(errorResponse)
        }
    }
}