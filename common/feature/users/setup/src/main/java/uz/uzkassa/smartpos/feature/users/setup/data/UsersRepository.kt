package uz.uzkassa.smartpos.feature.users.setup.data

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UsersRepository {

    fun getUsers(): Flow<List<User>>
}