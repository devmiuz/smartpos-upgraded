package uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository.params.ChangeUserPhoneNumberParams

internal interface UserDataRepository {

    fun changeUserPhoneNumber(params: ChangeUserPhoneNumberParams): Flow<User>
}