package uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.mapper

import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model.RefreshAuth
import uz.uzkassa.smartpos.core.data.source.resource.auth.refresh.model.RefreshAuthResponse

fun RefreshAuthResponse.map() =
    RefreshAuth(accessToken, refreshToken)