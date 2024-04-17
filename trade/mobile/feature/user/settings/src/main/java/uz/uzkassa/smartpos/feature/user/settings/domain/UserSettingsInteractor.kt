package uz.uzkassa.smartpos.feature.user.settings.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.settings.data.repository.UserRepository
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureArgs
import javax.inject.Inject

internal class UserSettingsInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val userRepository: UserRepository,
    private val userSettingsFeatureArgs: UserSettingsFeatureArgs
) {

    fun isChangePasswordAllowed(): Boolean =
        userSettingsFeatureArgs.userRoleType != UserRole.Type.CASHIER

    fun getCurrentUser(): Flow<Result<User>> =
        userRepository
            .getCurrentUser()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
}