package uz.uzkassa.smartpos.feature.user.list.data.delete

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.feature.user.list.data.delete.params.DeleteUserParams
import javax.inject.Inject

internal class UserDeleteRepositoryImpl @Inject constructor(
    private val userEntityDao: UserEntityDao,
    private val userRestService: UserRestService
) : UserDeleteRepository {

    @FlowPreview
    override fun deleteUserById(params: DeleteUserParams): Flow<Unit> {
        return userRestService
            .deleteUser(params.id)
            .onEach { userEntityDao.deleteById(params.id) }
            .map { Unit }
    }
}