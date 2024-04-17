package uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.UnauthorizedHttpException
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.user.confirmation.data.exception.InvalidConfirmationCredentialsException
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner.params.OwnerConfirmationParams
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureArgs
import javax.inject.Inject

internal class OwnerConfirmationRepositoryImpl @Inject constructor(
    private val userAuthRestService: UserAuthRestService,
    private val userConfirmationFeatureArgs: UserConfirmationFeatureArgs,
    private val userEntityDao: UserEntityDao,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService,
    private val userRoleEntityDao: UserRoleEntityDao,
    private val userRoleRestService: UserRoleRestService
) : OwnerConfirmationRepository {
    private val branchId: Long = userConfirmationFeatureArgs.branchId

    @FlowPreview
    override fun confirmOwner(params: OwnerConfirmationParams): Flow<Unit> {
        return flow { emit(userRelationDao.getRelationsByBranchId(branchId)) }
            .switch {
                userRoleRestService
                    .getUserRoles()
                    .onEach { userRoleEntityDao.insertOrUpdate(it.mapToEntities()) }
                    .flatMapConcat { userRestService.getUsersByBranchId(branchId) }
                    .onEach { userEntityDao.save(it) }
                    .map { userRelationDao.getRelationsByBranchId(branchId) }
            }
            .map { it -> it.map().find { it.userRole.type == UserRole.Type.OWNER } }
            .filterNotNull()
            .flatMapConcat { userAuthRestService.authenticate(params.asJsonElement(it.phoneNumber)) }
            .catch {
                throw if (it is UnauthorizedHttpException) InvalidConfirmationCredentialsException()
                else it
            }
            .map { Unit }
    }
}