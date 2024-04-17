package uz.uzkassa.smartpos.feature.account.recovery.password.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params.ActivateRecoveryParams
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params.AuthenticateParams
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.params.FinishRecoveryParams
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureArgs
import javax.inject.Inject

internal class RecoveryPasswordRepositoryImpl @Inject constructor(
    private val accountRestService: AccountAuthRestService,
    private val accountRecoveryPasswordFeatureArgs: AccountRecoveryPasswordFeatureArgs,
    private val deviceInfoManager: DeviceInfoManager
) : RecoveryPasswordRepository {

    private val phoneNumber: String
        get() = accountRecoveryPasswordFeatureArgs.phoneNumber

    private val serialNumber: String
        get() = deviceInfoManager.deviceInfo.serialNumber

    override fun getRequestedPhoneNumber(): String =
        phoneNumber

    override fun requestRecovery(): Flow<Unit> =
        accountRestService.requestRecoveryPassword(phoneNumber)

    @FlowPreview
    override fun activateRecovery(params: ActivateRecoveryParams): Flow<Unit> {
        return accountRestService
            .finishRecoveryPassword(params.asJsonElement(phoneNumber, serialNumber))
            .flatMapConcat {
                val jsonElement: JsonElement =
                    AuthenticateParams(phoneNumber, serialNumber).asJsonElement()
                return@flatMapConcat accountRestService.authenticate(jsonElement)
                    .map { Unit }
            }
    }

    override fun finishRecovery(params: FinishRecoveryParams): Flow<Unit> {
        return accountRestService.changePassword(params.asJsonElement(serialNumber))
    }
}