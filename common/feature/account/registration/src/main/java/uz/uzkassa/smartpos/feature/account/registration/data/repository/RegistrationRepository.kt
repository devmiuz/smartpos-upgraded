package uz.uzkassa.smartpos.feature.account.registration.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.ActivateRegistrationParams
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.FinishRegistrationParams
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.RequestConfirmationCodeParams

internal interface RegistrationRepository {

    fun requestConfirmationCode(params: RequestConfirmationCodeParams): Flow<Unit>

    fun activateRegistration(params: ActivateRegistrationParams): Flow<Unit>

    fun finishRegistration(params: FinishRegistrationParams): Flow<Unit>
}