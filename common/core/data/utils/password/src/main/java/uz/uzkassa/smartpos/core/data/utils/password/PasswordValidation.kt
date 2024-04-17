package uz.uzkassa.smartpos.core.data.utils.password

import java.io.Serializable

data class PasswordValidation internal constructor(
    val matchesUppercase: Boolean,
    val matchesLowercase: Boolean,
    val matchesLength: Boolean,
    val isValid: Boolean
) : Exception() {

    fun getException(): Exception = Exception(
        matchesUppercase = matchesUppercase,
        matchesLowercase = matchesLowercase,
        matchesLength = matchesLength,
        isValid = isValid
    )

    data class Exception internal constructor(
        val matchesUppercase: Boolean,
        val matchesLowercase: Boolean,
        val matchesLength: Boolean,
        val isValid: Boolean
    ) : kotlin.Exception(), Serializable

    companion object {

        fun validate(password: String): PasswordValidation {
            val matchesUppercase: Boolean = password.matches(".*\\p{Lu}+.*".toRegex())
            val matchesLowercase: Boolean = password.matches(".*\\p{Ll}+.*".toRegex())
            val matchesLength: Boolean = password.matches("^.{9,}\$".toRegex())
            val isValid: Boolean = matchesUppercase && matchesLowercase && matchesLength
            return PasswordValidation(matchesUppercase, matchesLowercase, matchesLength, isValid)
        }
    }
}