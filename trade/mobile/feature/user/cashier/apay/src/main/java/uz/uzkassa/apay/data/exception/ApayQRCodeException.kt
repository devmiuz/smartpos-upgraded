package uz.uzkassa.apay.data.exception

import uz.uzkassa.apay.data.model.ApayQRCodeExceptionBody
import java.io.IOException
import java.io.Serializable

data class ApayQRCodeException(
    val response: ApayQRCodeExceptionBody
) : IOException(), Serializable
