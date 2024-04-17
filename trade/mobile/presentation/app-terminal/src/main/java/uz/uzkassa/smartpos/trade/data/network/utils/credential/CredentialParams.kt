package uz.uzkassa.smartpos.trade.data.network.utils.credential

interface CredentialParams {
    var accessToken: String?
    val basicAuth: String
    var refreshToken: String
    val isResumed: Boolean
}