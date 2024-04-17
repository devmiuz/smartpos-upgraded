package uz.uzkassa.smartpos.trade.auth.dependencies

import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface AuthFeatureDependencies {

    val authFeatureArgs: AuthFeatureArgs

    val authFeatureCallback: AuthFeatureCallback

    val coroutineContextManager: CoroutineContextManager
}