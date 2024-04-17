package uz.uzkassa.smartpos.core.manager.coroutine

import kotlin.coroutines.CoroutineContext

interface CoroutineContextManager {

    val ioContext: CoroutineContext

    val defaultContext: CoroutineContext

    val mainContext: CoroutineContext

    val unconfinedContext: CoroutineContext

    companion object {

        fun instantiate(): CoroutineContextManager =
            CoroutineContextManagerImpl()
    }
}