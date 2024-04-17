package uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.user

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

interface UserRepository {

    fun getUser(): Flow<User>

    fun logoutUser(): Flow<Unit>
}