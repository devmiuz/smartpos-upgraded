package uz.uzkassa.smartpos.feature.user.settings.password.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.settings.password.data.repository.params.UserChangePasswordParams

internal interface UserAuthRepository {

    fun changePassword(params: UserChangePasswordParams): Flow<Unit>
}