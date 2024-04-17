package uz.uzkassa.smartpos.feature.account.auth.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.account.service.AccountRestService
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface AccountAuthFeatureDependencies {

    val accountAuthFeatureCallback: AccountAuthFeatureCallback

    val accountAuthRestService: AccountAuthRestService

    val accountRestService: AccountRestService

    val coroutineContextManager: CoroutineContextManager
}