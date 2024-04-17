package uz.uzkassa.smartpos.trade.manager.coroutine

import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

class CoroutineContextManagerProviderImpl : CoroutineContextManagerProvider {

    override val coroutineContextManager by lazy {
        CoroutineContextManager.instantiate()
    }
}