package uz.uzkassa.smartpos.feature.account.registration.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.ActivateRegistrationParams
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.AuthenticateParams
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.FinishRegistrationParams
import uz.uzkassa.smartpos.feature.account.registration.data.repository.params.RequestConfirmationCodeParams
import javax.inject.Inject

internal class RegistrationRepositoryImpl @Inject constructor(
    private val accountAuthRestService: AccountAuthRestService,
    deviceInfoManager: DeviceInfoManager
) : RegistrationRepository {
    private val serialNumber: String = deviceInfoManager.deviceInfo.serialNumber

    override fun requestConfirmationCode(params: RequestConfirmationCodeParams): Flow<Unit> {
        return accountAuthRestService.requestRegistrationConfirmationSmsCode(params.asJsonElement())
    }

    @FlowPreview
    override fun activateRegistration(params: ActivateRegistrationParams): Flow<Unit> {
        return accountAuthRestService
            .activateRegistrationBySms(params.asJsonElement(serialNumber))
            .flatMapConcat {
                val jsonElement: JsonElement =
                    AuthenticateParams(params.phoneNumber, serialNumber).asJsonElement()
                return@flatMapConcat accountAuthRestService.authenticate(jsonElement)
                    .map { Unit }
            }
    }

    override fun finishRegistration(params: FinishRegistrationParams): Flow<Unit> {
        return accountAuthRestService.changePassword(params.asJsonElement(serialNumber))
    }
}