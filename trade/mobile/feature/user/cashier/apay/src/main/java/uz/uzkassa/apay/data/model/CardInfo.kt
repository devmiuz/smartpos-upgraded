package uz.uzkassa.apay.data.model

data class CardInfo(
    val cardNumber: String,
    val cardExpiryDate: String,
    val isPaymentAllowed: Boolean = false
)