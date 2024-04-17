package uz.uzkassa.smartpos.feature.users.setup.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.users.setup.data.UsersRepository
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureArgs
import javax.inject.Inject

internal class UsersInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val usersRepository: UsersRepository,
    usersSetupFeatureArgs: UsersSetupFeatureArgs
) {
    val branchId: Long = usersSetupFeatureArgs.branchId

    fun getUsers(): Flow<Result<List<User>>> {
        return usersRepository
            .getUsers()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}