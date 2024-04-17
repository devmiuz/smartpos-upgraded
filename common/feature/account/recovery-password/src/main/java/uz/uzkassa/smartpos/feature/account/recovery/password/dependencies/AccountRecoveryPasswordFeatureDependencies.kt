package uz.uzkassa.smartpos.feature.account.recovery.password.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface AccountRecoveryPasswordFeatureDependencies {

    val accountAuthRestService: AccountAuthRestService

    val accountRecoveryPasswordFeatureArgs: AccountRecoveryPasswordFeatureArgs

    val accountRecoveryPasswordFeatureCallback: AccountRecoveryPasswordFeatureCallback

    val coroutineContextManager: CoroutineContextManager

    val deviceInfoManager: DeviceInfoManager
}