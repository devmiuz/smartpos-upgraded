package uz.uzkassa.smartpos.feature.user.settings.password.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.feature.user.settings.password.data.repository.params.UserChangePasswordParams
import javax.inject.Inject

internal class UserAuthRepositoryImpl @Inject constructor(
    private val userAuthRestService: UserAuthRestService
) : UserAuthRepository {

    override fun changePassword(params: UserChangePasswordParams): Flow<Unit> {
        return userAuthRestService.changePassword(params.asJsonElement())
    }
}