package uz.uzkassa.smartpos.feature.user.saving.data.repository.user

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UserRepository {

    fun getUser(): Flow<User>
}