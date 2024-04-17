package uz.uzkassa.smartpos.feature.user.list.data.delete

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.list.data.delete.params.DeleteUserParams

internal interface UserDeleteRepository {

    fun deleteUserById(params: DeleteUserParams): Flow<Unit>
}