package uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.settings.language.data.repository.user.params.ChangeUserLanguageParams

internal interface UserDataRepository {

    fun getUser(): Flow<User>

    fun changeUserLanguage(params: ChangeUserLanguageParams): Flow<User>
}