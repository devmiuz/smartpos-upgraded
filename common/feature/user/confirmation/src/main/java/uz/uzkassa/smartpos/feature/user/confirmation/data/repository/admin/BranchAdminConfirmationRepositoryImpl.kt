package uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.model.UserAuthResponse
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.UnauthorizedHttpException
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.user.confirmation.data.exception.InvalidConfirmationCredentialsException
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin.params.BranchAdminConfirmationParams
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureArgs
import javax.inject.Inject

internal class BranchAdminConfirmationRepositoryImpl @Inject constructor(
    private val userAuthRestService: UserAuthRestService,
    private val userConfirmationFeatureArgs: UserConfirmationFeatureArgs,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService,
    private val userEntityDao: UserEntityDao
) : BranchAdminConfirmationRepository {

    @Suppress("LABEL_NAME_CLASH")
    @FlowPreview
    override fun confirmBranchAdmin(params: BranchAdminConfirmationParams): Flow<Unit> {
        val branchId: Long = userConfirmationFeatureArgs.branchId
        return flow { emit(userRelationDao.getRelationsByBranchId(branchId)) }
            .switch {
                userRestService
                    .getUsersByBranchId(branchId)
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationsByBranchId(branchId) }
            }
            .map { it.map() }
            .map { it -> it.filter { it.userRole.type == UserRole.Type.BRANCH_ADMIN } }
            .flatMapConcat { authenticateBranchAdmin(it, params) }
            .map { Unit }
    }

    @FlowPreview
    private fun authenticateBranchAdmin(
        users: List<User>,
        params: BranchAdminConfirmationParams,
        index: Int = 0
    ): Flow<UserAuthResponse> {
        return flowOf(users[index])
            .flatMapConcat { userAuthRestService.authenticate(params.asJsonElement(it.phoneNumber)) }
            .catch {
                when (it) {
                    is UnauthorizedHttpException ->
                        authenticateBranchAdmin(users, params, index + 1)
                    is IndexOutOfBoundsException -> throw InvalidConfirmationCredentialsException()
                    else -> throw it
                }
            }
    }
}