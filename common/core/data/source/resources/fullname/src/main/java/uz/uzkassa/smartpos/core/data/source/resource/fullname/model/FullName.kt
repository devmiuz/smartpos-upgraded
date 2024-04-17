package uz.uzkassa.smartpos.core.data.source.resource.fullname.model

data class FullName(
    val firstName: String,
    val lastName: String?,
    val patronymic: String?
) {

    val fullName: String
        get() = if (!lastName.isNullOrBlank()) "$lastName $firstName" else firstName
}