package uz.uzkassa.smartpos.core.data.source.resource.auth.user.mapper

import uz.uzkassa.smartpos.core.data.source.resource.auth.user.model.UserAuth
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.model.UserAuthResponse

fun UserAuthResponse.map() =
    UserAuth(accessToken, refreshToken)