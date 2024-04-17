package uz.uzkassa.smartpos.core.data.source.resource.auth.account.model

data class AccountAuth(
    val accessToken: String,
    val refreshToken: String? = null
)