package uz.uzkassa.smartpos.core.data.source.resource.user

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.store.Store
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService

class UserStore(
    private val userEntityDao: UserEntityDao,
    private val userRelationDao: UserRelationDao,
    private val userRestService: UserRestService,
    private val userRoleEntityDao: UserRoleEntityDao,
    private val userRoleRestService: UserRoleRestService
) {

    @FlowPreview
    fun getUserByBranchId(): Store<Long, User> {
        return Store.from<Long, UserResponse, User>(
            fetcher = Fetcher.ofFlow { it ->
                userRestService.getUserByUserId(it)
                    .flatMapConcat { response ->
                        userRoleRestService.getUserRoles()
                            .onEach { userRoleEntityDao.insertOrUpdate(it.mapToEntities()) }
                            .map { response }
                    }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { userId ->
                    userRelationDao
                        .getRelationFlowByUserId(userId)
                        .filter { it.userEntity.id == userId }
                        .map { it.map() }
                },
                writer = { _, it -> userEntityDao.save(it) }
            )
        )
    }

    @FlowPreview
    fun getUsers(): Store<Unit, List<User>> {
        return Store.from<Unit, List<UserResponse>, List<User>>(
            fetcher = Fetcher.ofFlow {
                userRestService.getUsers()
                    .flatMapConcat { list ->
                        userRoleRestService.getUserRoles()
                            .onEach { userRoleEntityDao.insertOrUpdate(it.mapToEntities()) }
                            .map { list }
                    }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { userRelationDao.getRelationsFlow().map { it.map() } },
                writer = { _, it -> userEntityDao.save(it) }
            )
        )
    }

    @FlowPreview
    fun getUsersByBranchId(): Store<Long, List<User>> {
        return Store.from<Long, List<UserResponse>, List<User>>(
            fetcher = Fetcher.ofFlow { it ->
                userRestService.getUsersByBranchId(it)
                    .flatMapConcat { list ->
                        userRoleRestService.getUserRoles()
                            .onEach { userRoleEntityDao.insertOrUpdate(it.mapToEntities()) }
                            .map { list }
                    }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { userRelationDao.getRelationsFlow().map { it.map() } },
                writer = { _, it -> userEntityDao.save(it) }
            )
        )
    }
}