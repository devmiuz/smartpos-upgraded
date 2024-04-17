package uz.uzkassa.smartpos.core.data.source.resource.auth.user.model

data class UserAuth(
    val accessToken: String,
    val refreshToken: String? = null
)