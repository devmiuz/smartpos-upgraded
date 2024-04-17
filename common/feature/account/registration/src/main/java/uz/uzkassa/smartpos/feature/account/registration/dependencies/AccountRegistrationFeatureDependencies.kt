package uz.uzkassa.smartpos.feature.account.registration.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface AccountRegistrationFeatureDependencies {

    val accountAuthRestService: AccountAuthRestService

    val accountRegistrationFeatureCallback: AccountRegistrationFeatureCallback

    val coroutineContextManager: CoroutineContextManager

    val deviceInfoManager: DeviceInfoManager
}