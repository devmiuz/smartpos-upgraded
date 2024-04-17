package uz.uzkassa.smartpos.core.data.source.resource.auth.account.mapper

import uz.uzkassa.smartpos.core.data.source.resource.auth.account.model.AccountAuth
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.model.AccountAuthResponse

fun AccountAuthResponse.map() =
    AccountAuth(accessToken, refreshToken)