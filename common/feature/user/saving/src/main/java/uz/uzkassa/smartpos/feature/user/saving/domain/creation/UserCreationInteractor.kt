package uz.uzkassa.smartpos.feature.user.saving.domain.creation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.saving.data.model.creation.UserCreationData
import uz.uzkassa.smartpos.feature.user.saving.data.repository.branch.BranchRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role.UserRoleRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.UserSavingRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.params.SaveUserParams
import uz.uzkassa.smartpos.feature.user.saving.domain.UserSaveInteractor
import java.util.*
import javax.inject.Inject

internal class UserCreationInteractor @Inject constructor(
    private val branchRepository: BranchRepository,
    private val coroutineContextManager: CoroutineContextManager,
    private val userRoleRepository: UserRoleRepository,
    private val userSaveRepository: UserSavingRepository
) : UserSaveInteractor() {

    fun getUserCreationData(): Flow<UserCreationData> {
        return branchRepository
            .getBranches()
            .zip(userRoleRepository.getUserRoles()) { t1, t2 -> UserCreationData(t1, t2) }
            .flowOn(coroutineContextManager.ioContext)
    }

    fun createUser(): Flow<Result<User>> {
        return proceedWithResult { data ->
            userSaveRepository
                .createUser(
                    SaveUserParams(
                        branchId = data.branch.id,
                        phoneNumber = data.phoneNumber,
                        userRole = data.userRole,
                        startDate = data.startDate ?: Date(),
                        fullName = FullName(data.firstName, data.lastName, data.patronymic)
                    )
                )
                .flowOn(coroutineContextManager.ioContext)
        }
    }
}