package uz.uzkassa.smartpos.feature.account.recovery.password.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params.ActivateRecoveryParams
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params.FinishRecoveryParams

internal interface RecoveryPasswordRepository {

    fun getRequestedPhoneNumber(): String

    fun requestRecovery(): Flow<Unit>

    fun activateRecovery(params: ActivateRecoveryParams): Flow<Unit>

    fun finishRecovery(params: FinishRecoveryParams): Flow<Unit>
}