package uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.params.SaveUserParams

internal interface UserSavingRepository {

    fun createUser(params: SaveUserParams): Flow<User>

    fun updateUser(params: SaveUserParams): Flow<User>
}