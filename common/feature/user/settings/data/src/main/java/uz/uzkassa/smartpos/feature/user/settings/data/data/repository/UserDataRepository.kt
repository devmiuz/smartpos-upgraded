package uz.uzkassa.smartpos.feature.user.settings.data.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.settings.data.data.repository.params.ChangeUserDataParams

internal interface UserDataRepository {

    fun getUser(): Flow<User>

    fun changeUserData(params: ChangeUserDataParams): Flow<User>
}