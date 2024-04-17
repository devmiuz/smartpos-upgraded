package uz.uzkassa.smartpos.feature.user.saving.domain.update

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.saving.data.model.update.UserUpdateData
import uz.uzkassa.smartpos.feature.user.saving.data.repository.branch.BranchRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role.UserRoleRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.UserSavingRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.params.SaveUserParams
import uz.uzkassa.smartpos.feature.user.saving.domain.UserSaveInteractor
import javax.inject.Inject
import kotlin.properties.Delegates

internal class UserUpdateInteractor @Inject constructor(
    private val branchRepository: BranchRepository,
    private val coroutineContextManager: CoroutineContextManager,
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val userSavingRepository: UserSavingRepository
) : UserSaveInteractor() {
    private var user: User by Delegates.notNull()

    @FlowPreview
    fun getUserUpdateData(): Flow<Result<UserUpdateData>> {
        return userRepository
            .getUser()
            .onEach {
                user = it
                setPhoneNumber(it.phoneNumber)
                setUserRole(it.userRole)
                setLastName(it.fullName.lastName)
                setFirstName(it.fullName.firstName)
            }
            .zip(branchRepository.getBranches()) { t1, t2 -> Pair(t2, t1) }
            .zip(userRoleRepository.getUserRoles()) { pair, userRoles ->
                val branch: Branch =
                    checkNotNull(pair.first.find { it.id == pair.second.branchId })
                setBranch(branch)
                return@zip UserUpdateData(branch, pair.first, pair.second, userRoles)
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun updateUser(): Flow<Result<User>> {
        return proceedWithResult { data ->
            userSavingRepository
                .updateUser(
                    SaveUserParams(
                        id = user.id,
                        branchId = data.branch.id,
                        phoneNumber = data.phoneNumber,
                        userRole = data.userRole,
                        startDate = data.startDate,
                        fullName = FullName(data.firstName, data.lastName, data.patronymic)
                    )
                )
                .flowOn(coroutineContextManager.ioContext)
        }
    }
}