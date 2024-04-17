package uz.uzkassa.apay.data.exception

data class PaymentValidationException(
    val isCardNumberNotValid: Boolean,
    val isCardExpiryDateNotValid: Boolean,
    val isPaidAmountNotValid: Boolean
) : Exception() {

    internal val isPassed: Boolean
        get() = isCardNumberNotValid || isCardExpiryDateNotValid || isPaidAmountNotValid
}
