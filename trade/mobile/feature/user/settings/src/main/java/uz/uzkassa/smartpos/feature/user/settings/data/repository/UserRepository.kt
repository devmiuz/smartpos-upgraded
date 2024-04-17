package uz.uzkassa.smartpos.feature.user.settings.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UserRepository {

    fun getCurrentUser(): Flow<User>
}