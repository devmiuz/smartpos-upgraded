package uz.uzkassa.smartpos.trade.companion.manager.coroutine

import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.trade.companion.manager.coroutine.CoroutineContextManagerProvider

class CoroutineContextManagerProviderImpl : CoroutineContextManagerProvider {

    override val coroutineContextManager by lazy {
        CoroutineContextManager.instantiate()
    }
}