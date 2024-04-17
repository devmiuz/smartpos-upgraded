package uz.uzkassa.smartpos.trade.manager.coroutine

import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CoroutineContextManagerProvider {

    val coroutineContextManager: CoroutineContextManager

    companion object {

        fun instantiate(): CoroutineContextManagerProvider =
            CoroutineContextManagerProviderImpl()
    }
}