package uz.uzkassa.apay.data.model

import java.io.Serializable

interface ApayQRCodeExceptionBody: Serializable {
    val title: String
    val status: Int
    val path: String
    val message: String
}