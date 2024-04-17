package uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model

data class RefreshAuth(
    val accessToken: String,
    val refreshToken: String? = null
)