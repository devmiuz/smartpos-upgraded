package uz.uzkassa.apay.data.model

data class CreatePayment(
    val transactionId: Int,
    val maskedPhoneNumber: String,
    val errorMessage: String?,
    val status: String
)